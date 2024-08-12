package com.langtool.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    
    @GetMapping()
    public List<CollectionDto> gCollections() {
        // todo
        System.out.println("getting collections");
        return collectionService.getCollections();
    }


  
}