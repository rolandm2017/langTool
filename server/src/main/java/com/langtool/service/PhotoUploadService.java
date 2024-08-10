package com.langtool.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.langtool.client.GoogleCloudVisionApiService;
import com.langtool.model.PhotoEntity;
import com.langtool.repository.PhotoRepository;
import com.langtool.repository.PhotoTextRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;

@Service
public class PhotoUploadService {

    @Autowired
    private GoogleCloudVisionApiService googleCloudVisionApi;

    @Autowired
    private PhotoTextRepository photoTextRepository;
    @Autowired
    private PhotoRepository photoCollectionRepository;

    @Value("${file.upload-dir}")
    private String BASE_UPLOAD_DIR;
    @Value("${file.temp-dir}")
    private String TEMP_DIR;

    public void savePhotos(MultipartFile[] files) throws Exception {
        int someUserId = 500; // get from user auth later
        
        String userFolderPath = BASE_UPLOAD_DIR + "/" + Integer.toString(someUserId);
        
        File userFolder = new File(userFolderPath);

        // If the user's folder doesn't exist, create one
        createUserFolderIfNotExists(userFolder, someUserId);

        // Create a list to hold files that don't already exist
        List<MultipartFile> filesToParse = new ArrayList<>();

        // Check each file and add to filesToParse if it doesn't exist
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            Path filePath = Paths.get(userFolderPath, originalFilename);

            boolean fileWithThatNameExists = Files.exists(filePath);
            if (!fileWithThatNameExists) {
                filesToParse.add(file);
            }
        }
        
        // For each new file:
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            Path filePath = Paths.get(userFolderPath, originalFilename);  // joins a path and a file name to get a full file path.

            // Check if file already exists
            int counter = 1;
            boolean fileAlreadyExists = Files.exists(filePath);
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

            // Write the file
            Files.write(filePath, file.getBytes());
        }

        // Now gather the text from the photos
        for (MultipartFile file: filesToParse) {
            File converted = convertMultiPartToFile(file);
            String[] gatheredText = passPhotoToGoogleCloudVision(converted);

            PhotoEntity newPhoto = new PhotoEntity(file.getOriginalFilename(), null);
            PhotoEntity savedPhoto = photoCollectionRepository.save(newPhoto); // store the photo's path and get its id for the next write.

            Long newPhotoInsertId = savedPhoto.getId();
            // todo: store the gathered text
            photoTextRepository.insertMultiplePhotoTexts(newPhotoInsertId, gatheredText);
        }
    }

    private String[] passPhotoToGoogleCloudVision(File photo) {
        // todo: keep the api query separate.
        try {
            String response = googleCloudVisionApi.analyzeImageUsingGoogle(photo);
            String[] words = response.split(" ");
            for (String item : words) {
                System.out.println("Word from google: " + item);
            }
            return words;
        } catch (IOException e) {
            String errorMessage = "Error analyzing image: " + e.getMessage();
            System.out.println(errorMessage); // Console log the error message
            return new String[]{errorMessage}; // Return the error message in a single-element array
        }
    }

    private void createUserFolderIfNotExists(File folder, int userId) throws IOException {
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new IOException("Failed to create directory for user: " + userId);
            }
        }
    }

    private String appendCounterToFileNameToMakeUnique(String originalName, int counter, String extension) {
        return originalName + "_" + counter + extension;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }

    // New: Method to save a chunk of a file
    public void savePhotoChunk(MultipartFile file, String fileName, int chunkNumber, int totalChunks) throws IOException {
        File tempDir = new File(TEMP_DIR + fileName);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        File chunk = new File(tempDir, "chunk_" + chunkNumber);
        try (FileOutputStream fos = new FileOutputStream(chunk)) {
            fos.write(file.getBytes());
        }
    }

    // New: Method to process the complete file when all chunks are received
    public void processCompletePhoto(String fileName) throws IOException {
        File tempDir = new File(TEMP_DIR + fileName);
        File outputFile = new File(BASE_UPLOAD_DIR + fileName);

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            File[] chunks = tempDir.listFiles();
            if (chunks != null) {
                Arrays.sort(chunks, Comparator.comparing(f -> Integer.parseInt(f.getName().split("_")[1])));
                for (File chunk : chunks) {
                    Files.copy(chunk.toPath(), fos);
                }
            }
        }

        // Clean up temp directory
        deleteDirectory(tempDir);
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
}