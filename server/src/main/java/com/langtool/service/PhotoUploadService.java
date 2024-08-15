package com.langtool.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.vision.v1.Word;
import com.langtool.client.PhotoToTextFacade;
import com.langtool.dto.PhotoDto;
import com.langtool.model.CollectionEntity;
import com.langtool.model.PhotoEntity;
import com.langtool.model.TextGroupEntity;
import com.langtool.model.WordEntity;
import com.langtool.repository.CollectionRepository;
import com.langtool.repository.PhotoRepository;
import com.langtool.repository.TextGroupRepository;
import com.langtool.repository.WordRepository;

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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.HashSet;

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
    @Autowired
    private WordRepository wordRepository;

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
                photo = writeNewPhotoToDb(fileName, creationTime, intendedCollectionId);
                // process photo text
                logger.info("Passing photo to cloud vision");
                String[] gatheredTextArr = passPhotoToGoogleCloudVision(reassembledPhoto);
                List<WordEntity> newWordsToAdd = convertCloudVisionContentToWordEntities(gatheredTextArr);
                // TODO: move away from a native query -> make the ORM do it for you using getter,setter
                writeNewTextGroupToDb(intendedCollectionId, photo, newWordsToAdd);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }

        writeNewCollectionOrUpdate(intendedCollectionId, creationTime, photo);
        System.out.println("/  /  \\\\/  \\\\  \\\\          ^^^^    \\    ^^^^");
        deleteDirectory(tempDir); // Clean up temp directory
    }

    private List<WordEntity> convertCloudVisionContentToWordEntities(String[] gatheredText) {
        List<WordEntity> out = new ArrayList<WordEntity>();
        for (String text: gatheredText) {
            Optional<WordEntity> existingRow = wordRepository.findByOrigin(text);
            if (existingRow.isPresent()) {
                // increment
                WordEntity row = existingRow.get();
                row.incrementMentions();
                wordRepository.save(row);
                out.add(row);
                continue;
            }
            WordEntity topic = new WordEntity();
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

    private PhotoEntity writeNewPhotoToDb(String photoFileName, LocalDateTime creationTime, Long intendedCollectionId) {
        System.out.println("writing photo to db: " + photoFileName);
        Optional<CollectionEntity> someCollectionThatMightExist = collectionRepository.findById(intendedCollectionId);
        if (someCollectionThatMightExist.isPresent()) {
            // write the photo using that collection
            PhotoEntity newPhoto = new PhotoEntity(photoFileName, creationTime);
            newPhoto.setCollection(someCollectionThatMightExist.get());
            System.out.println("Saving text from " + photoFileName);
            // fixme: has to have a collection set
            PhotoEntity savedPhoto = photoRepository.save(newPhoto); // store the photo's path and get its id for the next write.
            return savedPhoto;
        }
        // if the collection isn't there yet, create it because
        // you can't create a photo that has no collection
        CollectionEntity fromScratch = new CollectionEntity();
        String defaultCollectionlabel = String.valueOf(creationTime);
        fromScratch.setId(intendedCollectionId);
        fromScratch.setLabel(defaultCollectionlabel);
        collectionRepository.save(fromScratch);

        PhotoEntity newPhoto = new PhotoEntity(photoFileName, creationTime);
        newPhoto.setCollection(fromScratch);
        System.out.println("Saving text from " + photoFileName);
        // fixme: has to have a collection set
        PhotoEntity savedPhoto = photoRepository.save(newPhoto); // store the photo's path and get its id for the next write.
        System.out.println("does this print? 202rm");
        return savedPhoto;
    }

    private void writeNewTextGroupToDb(Long associatedCollectionId, PhotoEntity newPhotoEntry, List<WordEntity> wordsToAdd) {
        logger.info("writing text group " + String.valueOf(wordsToAdd.size()));
        TextGroupEntity toSave = new TextGroupEntity();
        Set<WordEntity> uniqueWordsOnly = new HashSet<WordEntity>();
        Set<Long> seenIds = new HashSet<Long>();
        for (WordEntity word: wordsToAdd) {
            if (seenIds.contains(word.getId())) {
                continue;
            }
            seenIds.add(word.getId());
            uniqueWordsOnly.add(word);
        }
        toSave.setWords(uniqueWordsOnly);
        toSave.setPhoto(newPhotoEntry);
        textGroupRepository.save(toSave);
        // Long newPhotoInsertId = newPhotoEntry.getId();
        // textGroupRepository.insertTextGroup(associatedCollectionId, newPhotoInsertId, textArr);
    }

    private void writeNewCollectionOrUpdate(Long targetCollectionId, LocalDateTime creationTime, PhotoEntity photo) {
        /* Could be the first photo to be uploaded or one of dozens.
         * Hence the Collection might be being created or being updated.<
         * find if this target collection id already exists
         * */
        Optional<CollectionEntity> collectionEntityToUpdate = collectionRepository.findById(targetCollectionId);
        if (collectionEntityToUpdate.isPresent()) {
            System.out.println("add photo to collection: " + targetCollectionId.toString()+ " " + photo.toString());
            CollectionEntity collection = collectionEntityToUpdate.get();
            collection.addPhoto(photo);
            collectionRepository.save(collection);
            photo.setCollection(collection);
            photoRepository.save(photo);
            return;
        }
        // // if it doesn't exist, create a new one with just this one photo id
        CollectionEntity fromScratch = new CollectionEntity();
        fromScratch.addPhoto(photo);
        fromScratch.setCreationDate(creationTime);
        collectionRepository.save(fromScratch);
        photo.setCollection(fromScratch);
        photoRepository.save(photo);
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