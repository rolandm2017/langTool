package com.langtool.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PhotoUploadService {

    private static final String BASE_UPLOAD_DIR = "/path/to/upload/directory"; // Replace with your actual base path

    // todo: change the path to some dir in uhhh ... idk which folder would be best for this. google it

    public void savePhotos(MultipartFile[] files) throws Exception {
        int someUserId = 500;
        String userFolderPath = BASE_UPLOAD_DIR + File.separator + someUserId;
        File userFolder = new File(userFolderPath);

        if (!userFolder.exists()) {
            if (!userFolder.mkdirs()) {
                throw new IOException("Failed to create directory for user: " + someUserId);
            }
        }

        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            Path filePath = Paths.get(userFolderPath, originalFilename);

            // Check if file already exists
            int counter = 1;
            while (Files.exists(filePath)) {
                String nameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
                String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                String newFilename = nameWithoutExtension + "_" + counter + extension;
                filePath = Paths.get(userFolderPath, newFilename);
                counter++;
            }

            // Write the file
            Files.write(filePath, file.getBytes());
        }
    }
}