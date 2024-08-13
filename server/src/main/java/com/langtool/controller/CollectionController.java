package com.langtool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import com.langtool.object.PhotoText;
import com.langtool.service.CollectionService;

import com.langtool.dto.CollectionDto;

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
    public List<CollectionDto> gCollections() {
        // todo
        System.out.println("getting collections");
        return collectionService.getCollections();
    }


  
}