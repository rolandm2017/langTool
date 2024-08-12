package com.langtool.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.langtool.client.PhotoToTextFacade;
import com.langtool.dto.PhotoDto;
import com.langtool.model.CollectionEntity;
import com.langtool.model.PhotoEntity;
import com.langtool.model.TextGroupEntity;
import com.langtool.repository.CollectionRepository;
import com.langtool.repository.PhotoRepository;
import com.langtool.repository.TextGroupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class PhotoUploadService {

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

    public List<PhotoDto> getAllPhotos() {
        logger.info("Getting all photos");
        List<PhotoEntity> all = photoRepository.findAll();
        List<PhotoDto> processed = new ArrayList<>();
        System.out.println("all # : " + Integer.toString(all.size()));
        for (PhotoEntity toConvert : all) {
            PhotoDto dto = convertPhotoEntityToPhotoDto(toConvert);
            processed.add(dto);
        }
        return processed;
    }

    private PhotoDto convertPhotoEntityToPhotoDto(PhotoEntity toConvert) {
        logger.info("Converting entity to dto");
        Long id = toConvert.getId();
        System.out.println("processing ... " + Long.toString(id));
        Optional<TextGroupEntity> photoText = textGroupRepository.findById(id);
        if (photoText.isPresent()) {
            String[] splitText = photoText.get().getText().split(" ");
            List<String> wordsToEmbed = Arrays.asList(splitText);
            System.out.println(Integer.valueOf(splitText.length));
            return new PhotoDto(
                toConvert.getId(),
                toConvert.getFilePath(),
                wordsToEmbed
            );
        }
        System.out.println(Long.toString(id) + " wasn't present");
        List<String> failure = new ArrayList<>();
        Long failureValue = null;
        return new PhotoDto(failureValue, "Error", failure);
        
    }

    public void savePhotoChunk(MultipartFile file, String fileName, int chunkNumber, int totalChunks) throws IOException {
        int someUserId = 500; // get from user auth later
        
        String userTempDir = TEMP_DIR + File.separator + Integer.toString(someUserId) + File.separator + fileName;
        File tempDir = new File(userTempDir);
        boolean noTempDirYet = !tempDir.exists();
        if (noTempDirYet) {
            tempDir.mkdirs();
        }

        File chunk = new File(tempDir, "chunk_" + chunkNumber);
        try (FileOutputStream fos = new FileOutputStream(chunk)) {
            fos.write(file.getBytes());
        }

        if (chunkNumber == totalChunks - 1) {
            processCompletePhoto(fileName, someUserId);
        }
    }

    private void processCompletePhoto(String fileName, int userId) throws IOException {

        logger.info("processing complete photo: " + fileName);
        String userTempDir = TEMP_DIR + File.separator + Integer.toString(userId) + File.separator + fileName;
        String userUploadDir = BASE_UPLOAD_DIR + File.separator + Integer.toString(userId);
        
        File tempDir = new File(userTempDir);
        File outputFile = new File(userUploadDir + File.separator + fileName);

        createParentFileDirectoryIfNotExists(outputFile);

        LocalDateTime creationTime = LocalDateTime.now();
       
        List<Long> photoIds = new ArrayList<>();
        System.out.println("128rm");
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            File[] chunks = tempDir.listFiles();
            if (chunks != null) {
                File reassembledPhoto = reassemblePhotoChunks(fileName, chunks, fos);
                System.out.println(reassembledPhoto.getName() + " 133rm");
                PhotoEntity savedPhoto = writeNewPhotoToDb(fileName, creationTime);
                photoIds.add(savedPhoto.getId());
                
                // process photo text
                System.out.println("138rm 138rm 138rm 138rm ");
                logger.info("Passing photo to cloud vision");
                String[] gatheredTextArr = passPhotoToGoogleCloudVision(reassembledPhoto);
                
                writeTextGroupToDb(savedPhoto, gatheredTextArr);
                
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }

        writeNewCollection(creationTime, photoIds);

        deleteDirectory(tempDir); // Clean up temp directory
    }

    // Helper method
    private void createParentFileDirectoryIfNotExists(File outputFile) {
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }

    }

    // Helper method
    private File reassemblePhotoChunks(String fileName, File[] chunks, FileOutputStream fos) throws IOException {
        logger.info("Reassembling photo chunk: " + fileName);
        String[] nameAndExtension = fileName.split("\\.(?=[^\\.]+$)");
        logger.info(fileName + " " + nameAndExtension.length + " parts");

        Arrays.sort(chunks, Comparator.comparing(f -> Integer.parseInt(f.getName().split("_")[1])));

        // Create a temporary file for the reassembled photo
        System.out.println("=== 173rm === 173rm");
        System.out.println(nameAndExtension[0]);
        System.out.println(nameAndExtension[1]);

        // Create a file with the exact name in the current directory
        File reassembledFile = new File(nameAndExtension[0] + "." + nameAndExtension[1]);
        reassembledFile.deleteOnExit(); // Ensures the temp file is deleted when the JVM exits

        // Copy chunks directly to the reassembledFile
        try (FileOutputStream reassembledFos = new FileOutputStream(reassembledFile)) {
            for (File chunk : chunks) {
                Files.copy(chunk.toPath(), reassembledFos);
            }
        }

        // Copy the reassembled file to the provided FileOutputStream
        try (FileInputStream fis = new FileInputStream(reassembledFile)) {
            fis.transferTo(fos);
        }

        logger.info("Chunk reassembled");
        return reassembledFile;
    }

    private PhotoEntity writeNewPhotoToDb(String photoFileName, LocalDateTime creationTime) {
        logger.info("writing photo to db: " + photoFileName);
        PhotoEntity newPhoto = new PhotoEntity(photoFileName, creationTime);
        System.out.println("Saving text from " + photoFileName);
        PhotoEntity savedPhoto = photoRepository.save(newPhoto); // store the photo's path and get its id for the next write.
        return savedPhoto;
    }

    private void writeTextGroupToDb(PhotoEntity newPhotoEntry, String[] textArr) {
        logger.info("writing text group " + String.valueOf(textArr.length));
        Long newPhotoInsertId = newPhotoEntry.getId();
        textGroupRepository.insertTextGroup(newPhotoInsertId, textArr);
    }

    private void writeNewCollection(LocalDateTime creationDate, List<Long> photoIds) {
        logger.info("writing collection with ids: " + String.valueOf(photoIds));
        CollectionEntity newCollection = new CollectionEntity(creationDate.toString(), creationDate, photoIds);
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

   

    // Helper method to delete a directory
    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }


    private String[] passPhotoToGoogleCloudVision(File photo) {
        // keep the api query separate.
        try {
            String response = photoToTextFacade.convertPhotoToTextUsingGoogle(photo);
            String[] words = response.split(" ");
            for (String item : words) {
                System.out.println(photo.getName() + " -> Word from google: " + item);
            }
            return words;
        } catch (IOException e) {
            String errorMessage = "Error analyzing image: " + e.getMessage();
            System.out.println(errorMessage); // Console log the error message
            return new String[]{errorMessage}; // Return the error message in a single-element array
        }
    }

    private void info(String text) {
        logger.info(text);
    }

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

            writeTextGroupToDb(savedPhoto, gatheredTextArr);
        }
    }
}