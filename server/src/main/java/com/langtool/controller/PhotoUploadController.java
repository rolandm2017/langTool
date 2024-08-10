package com.langtool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.langtool.service.PhotoUploadService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/photos")
public class PhotoUploadController {

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");
    
    @Autowired 
    private PhotoUploadService photoUploadService;

    @PostMapping("/upload/small/files")
    public ResponseEntity<String> uploadSmallPhotos(@RequestParam("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body("No files were uploaded.");
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("One or more files are empty.");
            }

            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);

            if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
                return ResponseEntity.badRequest().body("File type not allowed. Allowed types: " + String.join(", ", ALLOWED_EXTENSIONS));
            }
        }

        try { 
            photoUploadService.savePhotos(files); 
            return ResponseEntity.ok("Files uploaded successfully");
        } catch (Exception e) { 
            return ResponseEntity.internalServerError().body("Error uploading files: " + e.getMessage()); 
        } 
    }

    @PostMapping("/upload/chunk")
    public ResponseEntity<String> uploadPhotoChunk(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileName") String fileName,
            @RequestParam("chunkNumber") int chunkNumber,
            @RequestParam("totalChunks") int totalChunks) {
        
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Chunk is empty.");
        }

        try {
            photoUploadService.savePhotoChunk(file, fileName, chunkNumber, totalChunks);
            
            if (chunkNumber == totalChunks - 1) {
                // All chunks received
                return ResponseEntity.ok("File upload complete: " + fileName);
            } else {
                // Chunk received, but not the last one
                return ResponseEntity.ok("Chunk " + (chunkNumber + 1) + " of " + totalChunks + " received for " + fileName);
            }
        } catch (IOException e) {
            // ## look here ##
            return ResponseEntity.internalServerError().body("Error uploading chunk: " + e.getMessage());
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}