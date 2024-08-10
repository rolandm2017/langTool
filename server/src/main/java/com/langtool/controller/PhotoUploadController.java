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

    // New: Chunked upload endpoint
    @PostMapping("/upload/chunk")
    public ResponseEntity<String> uploadPhotoChunk(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileName") String fileName,
            @RequestParam("chunkNumber") int chunkNumber,
            @RequestParam("totalChunks") int totalChunks) {
        
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Chunk is empty.");
        }

        String fileExtension = getFileExtension(fileName);
        if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            return ResponseEntity.badRequest().body("File type not allowed. Allowed types: " + String.join(", ", ALLOWED_EXTENSIONS));
        }

        try {
            // New: Save the chunk
            photoUploadService.savePhotoChunk(file, fileName, chunkNumber, totalChunks);
            
            if (chunkNumber == totalChunks - 1) {
                // New: All chunks received, process the complete file
                photoUploadService.processCompletePhoto(fileName);
                return ResponseEntity.ok("File uploaded and processed successfully");
            } else {
                return ResponseEntity.ok("Chunk received successfully");
            }
        } catch (IOException e) {
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