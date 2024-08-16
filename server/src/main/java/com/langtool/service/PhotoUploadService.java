package com.langtool.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.langtool.client.PhotoToTextFacade;
import com.langtool.dao.CollectionDao;
import com.langtool.dao.PhotoDao;
import com.langtool.dao.TextGroupDao;
import com.langtool.dao.WordDao;
import com.langtool.dto.EntityToDtoConverter;
import com.langtool.dto.PhotoDto;
import com.langtool.model.PhotoEntity;
import com.langtool.model.WordEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


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
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class PhotoUploadService {

    private static final Logger logger = LoggerFactory.getLogger(PhotoUploadService.class);

    @Autowired
    private PhotoToTextFacade photoToTextFacade;

    @Autowired
    private TextGroupDao textGroupDao;
    @Autowired
    private CollectionDao collectionDao;


    @Autowired
    private PhotoDao photoDao;
    @Autowired
    private WordDao wordDao;


    
    @Value("${file.upload-dir}")
    private String BASE_UPLOAD_DIR;
    @Value("${file.temp-dir}")
    private String TEMP_DIR;

    public List<PhotoDto> getAllPhotos() {
        logger.info("Getting all photos");
        List<PhotoEntity> all = photoDao.findAllPhotos();
        List<PhotoDto> processed = new ArrayList<>(); // todo: batch convert oneliner
        System.out.println("all # : " + Integer.toString(all.size()));
        for (PhotoEntity toConvert : all) {
            PhotoDto dto = EntityToDtoConverter.convertPhotoEntityToPhotoDto(toConvert);
            processed.add(dto);
        }
        return processed;
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
       
        PhotoEntity photo = null;
        System.out.println("128rm");
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            File[] chunks = tempDir.listFiles();
            if (chunks != null) {
                File reassembledPhoto = reassemblePhotoChunks(fileName, chunks, fos);
                System.out.println("reassembled! writing ");
                photo = photoDao.writeNewPhotoToDb(fileName, creationTime, intendedCollectionId);
                // process photo text
                logger.info("Passing photo to cloud vision");
                String[] gatheredTextArr = passPhotoToGoogleCloudVision(reassembledPhoto);
                System.out.println(gatheredTextArr.length);
                System.out.println("122rm");
                System.out.println("122rm");
                System.out.println("x 122rm");
                System.out.println("x 122rm");
                System.out.println("x 122rm");
                System.out.println("x 122rm");
                System.out.println("122rm");
                List<WordEntity> newWordsToAdd = convertCloudVisionContentToWordEntities(gatheredTextArr);
                // TODO: move away from a native query -> make the ORM do it for you using getter,setter
                System.out.println("132rm");
                textGroupDao.writeNewTextGroupToDb(intendedCollectionId, photo, newWordsToAdd);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }

        collectionDao.writeNewCollectionOrUpdate(intendedCollectionId, creationTime, photo);
        System.out.println("/  /  \\\\/  \\\\  \\\\          ^^^^    \\    ^^^^");
        deleteDirectory(tempDir); // Clean up temp directory
    }

    private List<WordEntity> convertCloudVisionContentToWordEntities(String[] gatheredText) {
        List<WordEntity> out = new ArrayList<WordEntity>();

        // String[] lines = gatheredText.split("\\n");
        String[] lines = gatheredText;

        for (String text: lines) {
            

            Optional<WordEntity> existingRow = wordDao.findWordByOrigin(text);
            if (existingRow.isPresent()) {
                // increment
                WordEntity row = existingRow.get();
                row.incrementMentions();
                wordDao.saveWord(row);
                out.add(row);
                continue;
            }
            WordEntity topic = new WordEntity();
            // System.out.println(text + " 149rm");
            // System.out.println(String.valueOf(gatheredText.length) + " 137rm");
            // System.out.println(String.valueOf(text.length()) + " 150rm");

            topic.setOrigin(text);
            out.add(topic);
        }
        return out;
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
            String[] words = response.split("\n");
            // for (String item : words) {
            //     System.out.println(photo.getName() + " -> Word from google: " + String.valueOf(item.length()));
            // }
            return words;
        } catch (IOException e) {
            String errorMessage = "Error analyzing image: " + e.getMessage();
            System.out.println(errorMessage); // Console log the error message
            return new String[]{errorMessage}; // Return the error message in a single-element array
        }
    }


 
}