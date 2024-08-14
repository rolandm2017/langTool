package com.langtool.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.*;

import com.langtool.object.PhotoText;
import com.langtool.service.CollectionService;

import com.langtool.dto.CollectionDto;
import com.langtool.errors.ErrorResponse;

@RestController
@RequestMapping("/api/photocollections")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    // @GetMapping("/{photoCollectionId}")
    // public List<PhotoText> getPhotoTexts(@PathVariable Long photoCollectionId) {
        
    //     return photoTexts;
    // }
    @GetMapping("/{photoCollectionId}")
    public CollectionDto getCollectionById(@PathVariable Long photoCollectionId) {
        return collectionService.getCollectionById(photoCollectionId);
    }

    @GetMapping("/next-id")
    public ResponseEntity<NextCollectionIdResponse> getNextCollectionId() {
        
        Long currentHighestCollectionId = collectionService.getHighestCollectionId();
        if (currentHighestCollectionId == null) {
            // only happens when the table has no entries whatsoever
            NextCollectionIdResponse response = new NextCollectionIdResponse(1L);
            return ResponseEntity.ok(response);    
        }
        System.out.println(currentHighestCollectionId);
        System.out.println("35rm");
        
        NextCollectionIdResponse response = new NextCollectionIdResponse(currentHighestCollectionId + 1);
        return ResponseEntity.ok(response);
    }

    // Inner class to represent the response
    private static class NextCollectionIdResponse {
        private final Long nextCollectionId;

        public NextCollectionIdResponse(Long nextCollectionId) {
            this.nextCollectionId = nextCollectionId;
        }

        public Long getNextCollectionId() {
            return nextCollectionId;
        }
    }
    
    @GetMapping()
    public List<CollectionDto> getCollections() {
        // todo
        System.out.println("getting collections");
        return collectionService.getCollections();
    }

    @PatchMapping("/update-label")
    public ResponseEntity<UpdateResponse> updateCollectionLabel(@RequestBody UpdateCollectionLabelRequest request) {
        try {
            System.out.println(request.toString() + "  :: 68rm");
            boolean success = collectionService.updateCollectionLabel(request.getId(), request.getLabel());        
            if (success) {
                return ResponseEntity.ok(new UpdateResponse(true));
            } else {
                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new UpdateResponse(false, "Collection not found or update failed"));
            }
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new UpdateResponse(false, "An unexpected error occurred: " + e.getMessage()));
        }
    }
  
}

class UpdateCollectionLabelRequest {
    @JsonProperty("idForRenaming")
    private String idForRenaming;

    @JsonProperty("newLabel")
    private String newLabel;
    
    // Getters and setters
    public String getId() {
        return idForRenaming;
    }

    public void setId(String id) {
        this.idForRenaming = id;
    }

    public String getLabel() {
        return newLabel;
    }

    public void setLabel(String label) {
        this.newLabel = label;
    }

    @Override
    public String toString() {
        return "UpdateCollectionLabelRequest{" +
               "idForRenaming='" + idForRenaming + '\'' +
               ", newLabel='" + newLabel + '\'' +
               '}';
    }
}
class UpdateResponse {
    private boolean success;
    private String message;

    public UpdateResponse(boolean success) {
        this.success = success;
    }

    public UpdateResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}