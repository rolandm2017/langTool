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

import static java.lang.StringTemplate.STR;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    public void savePhotoChunk(MultipartFile file, String fileName, int chunkNumber, int totalChunks, Long intendedCollectionId) throws IOException {
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
            processCompletePhoto(fileName, someUserId, intendedCollectionId);
        }
    }

    private void processCompletePhoto(String fileName, int userId, Long intendedCollectionId) throws IOException {

        logger.info("processing complete photo: " + fileName);
        String userTempDir = TEMP_DIR + File.separator + Integer.toString(userId) + File.separator + fileName;
        String userUploadDir = BASE_UPLOAD_DIR + File.separator + Integer.toString(userId);
        
        File tempDir = new File(userTempDir);
        File outputFile = new File(userUploadDir + File.separator + fileName);

        createParentFileDirectoryIfNotExists(outputFile);

        LocalDateTime creationTime = LocalDateTime.now();
       
        Long photoId = null;
        System.out.println("128rm");
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            File[] chunks = tempDir.listFiles();
            if (chunks != null) {
                File reassembledPhoto = reassemblePhotoChunks(fileName, chunks, fos);
                System.out.println("reassembled! writing ");
                PhotoEntity savedPhoto = writeNewPhotoToDb(fileName, creationTime);
                photoId = savedPhoto.getId();
                System.out.println(savedPhoto.getId() + " is the photo ID to save");
                // process photo text
                logger.info("Passing photo to cloud vision");
                String[] gatheredTextArr = passPhotoToGoogleCloudVision(reassembledPhoto);
                
                writeNewTextGroupToDb(savedPhoto, gatheredTextArr);
                
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }

        writeNewCollectionOrUpdate(intendedCollectionId, creationTime, photoId);
        System.out.println("           /  /  \\/  \\  \\");
        System.out.println("          ^^^^    \\    ^^^^");
        deleteDirectory(tempDir); // Clean up temp directory
    }

    // private void fillContainerRow(Long collectionId, LocalDateTime creationTime, List<Long> photoIds) {
    //     // todo
    //     collectionRepository.
    // }


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

    private void writeNewCollectionOrUpdate(Long targetCollectionId, LocalDateTime creationTime, Long photoId) {
        /* Could be the first photo to be uploaded or one of dozens.
         * Hence the Collection might be being created or being updated.<
         * find if this target collection id already exists
         * */

        
        boolean collectionExists = collectionRepository.existsById(targetCollectionId);
        List<CollectionEntity> all = collectionRepository.findAll();
        System.out.println("All Collection IDs: " + all.stream().map(CollectionEntity::getId).collect(Collectors.toList()));
        System.out.println("Collection exists? " + " " + String.valueOf(targetCollectionId) + " " + String.valueOf(collectionExists));
        // if it does, put the photo into there
        if (collectionExists) {
            System.out.println("add photo to collection: " + targetCollectionId.toString()+ " " + photoId.toString());
            collectionRepository.writeAdditionalPhotoIdToCollection(targetCollectionId, photoId);
            System.out.println("here be dragons");
            return;
        }
        System.out.println("Start a collection: " + targetCollectionId.toString()+ " " + photoId.toString());
        // if it doesn't exist, create a new one with just this one photo id
        System.out.println("prints 234rm");
        collectionRepository.insertNewCollectionWithOnePhoto(targetCollectionId, creationTime, photoId);
        System.out.println("doesn't print 235rm");

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
                System.out.println(photo.getName() + " -> Word from google: " + String.valueOf(item.length()));
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

 
}