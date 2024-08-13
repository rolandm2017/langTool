package com.langtool.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.langtool.client.PhotoToTextFacade;
import com.langtool.model.CollectionEntity;
import com.langtool.model.PhotoEntity;
import com.langtool.repository.CollectionRepository;
import com.langtool.repository.PhotoRepository;
import com.langtool.repository.TextGroupRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SmallFileService {

    private static final Logger logger = LoggerFactory.getLogger(PhotoUploadService.class);

    
    @Autowired
    private PhotoToTextFacade photoToTextFacade;

    @Autowired
    private TextGroupRepository textGroupRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private CollectionRepository collectionRepository;

    @Value("${file.upload-dir}")
    private String BASE_UPLOAD_DIR;
    @Value("${file.temp-dir}")
    private String TEMP_DIR;

    public void savePhotosWithSmallFileSizes(MultipartFile[] newFiles) throws Exception {
        int someUserId = 500; // get from user auth later

        LocalDateTime creationTime = LocalDateTime.now();
        
        String userFolderPath = BASE_UPLOAD_DIR + File.separator + Integer.toString(someUserId);
        
        File userFolder = new File(userFolderPath);

        createUserFolderIfNotExists(userFolder, someUserId);

        // Create a list to hold files that don't already exist
        List<MultipartFile> filesToParse = new ArrayList<>();

        // Check each file and add to filesToParse if it doesn't exist
        System.out.println(Integer.toString(newFiles.length) + " Here, 90rm");
        for (MultipartFile file : newFiles) {
            String originalFilename = file.getOriginalFilename();

            File destFile = new File(userFolder.getAbsolutePath() + File.separator + originalFilename);
            file.transferTo(destFile);
        }
        
        // For each new file:
        for (MultipartFile file : newFiles) {
            String originalFilename = file.getOriginalFilename();
            Path filePath = Paths.get(userFolderPath, originalFilename);  // joins a path and a file name to get a full file path.

            int counter = 1;
            boolean fileAlreadyExists = Files.exists(filePath);
            System.out.println(filePath.toString() + " :: " + Boolean.toString(fileAlreadyExists));
            

            while (fileAlreadyExists) {
                // If a file with the same name exists, append a counter to the filename to make it unique
                String nameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
                String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                String newFilename = appendCounterToFileNameToMakeUnique(nameWithoutExtension, counter, extension);
                filePath = Paths.get(userFolderPath, newFilename);
                
                counter++;
                // always false unless user uploads
                // foo.jpg when foo_1.jpg already exists, hence necessitating foo_2.jpg
                fileAlreadyExists = Files.exists(filePath); 
            }

            Files.write(filePath, file.getBytes());
        }

        // Now gather the text from the photos
        for (MultipartFile file: filesToParse) {
            System.out.println("Parsing text from: " + file.getOriginalFilename());
            File convertedFile = convertMultiPartToFile(file);
            String[] gatheredTextArr = passPhotoToGoogleCloudVision(convertedFile);
            List<String> gatheredText = Arrays.asList(gatheredTextArr);

            PhotoEntity savedPhoto = writeNewPhotoToDb(file.getOriginalFilename(), creationTime);

            writeNewTextGroupToDb(savedPhoto, gatheredTextArr);
        }
    }

    private String[] passPhotoToGoogleCloudVision(File photo) {
        // keep the api query separate.
        try {
            String response = photoToTextFacade.convertPhotoToTextUsingGoogle(photo);
            String[] words = response.split(" ");
            for (String item : words) {
                System.out.println(photo.getName() + " -> Word from google: " + String.valueOf(item.length()));
            }
            return words;
        } catch (IOException e) {
            String errorMessage = "Error analyzing image: " + e.getMessage();
            System.out.println(errorMessage); // Console log the error message
            return new String[]{errorMessage}; // Return the error message in a single-element array
        }
    }

    private PhotoEntity writeNewPhotoToDb(String photoFileName, LocalDateTime creationTime) {
        System.out.println("writing photo to db: " + photoFileName);
        PhotoEntity newPhoto = new PhotoEntity(photoFileName, creationTime);
        System.out.println("Saving text from " + photoFileName);
        PhotoEntity savedPhoto = photoRepository.save(newPhoto); // store the photo's path and get its id for the next write.
        return savedPhoto;
    }

    private void writeNewTextGroupToDb(PhotoEntity newPhotoEntry, String[] textArr) {
        logger.info("writing text group " + String.valueOf(textArr.length));
        Long newPhotoInsertId = newPhotoEntry.getId();
        textGroupRepository.insertTextGroup(newPhotoInsertId, textArr);
    }


    private void writeNewCollection(int intendedCollectionId, LocalDateTime creationDate, List<Long> photoIds) {
        logger.info("writing collection with ids: " + String.valueOf(photoIds));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String labelAsMmDdYyyy = creationDate.format(formatter);
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy :: HH:mm");
        // String formattedDateTime = dateTime.format(formatter);
        CollectionEntity newCollection = new CollectionEntity(labelAsMmDdYyyy, creationDate, photoIds);
        collectionRepository.save(newCollection);
    }

    private void createUserFolderIfNotExists(File folder, int userId) throws IOException {
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new IOException("Failed to create directory for user: " + userId);
            }
        }
    }

    private String appendCounterToFileNameToMakeUnique(String originalName, int counter, String extension) {
        return originalName + "___" + counter + extension;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }

}
