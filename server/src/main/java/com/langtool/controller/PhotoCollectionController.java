package com.langtool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import com.langtool.object.PhotoText;
import com.langtool.service.PhotoCollectionService;

import com.langtool.dto.CollectionDto;

@RestController
@RequestMapping("/api/photocollections")
public class PhotoCollectionController {

    @Autowired
    private PhotoCollectionService photoCollectionService;

    @GetMapping("/{photoCollectionId}")
    public List<PhotoText> getPhotoTexts(@PathVariable Long photoCollectionId) {
        // TODO: Implement the logic to fetch photo texts based on the photoCollectionId
        // This is a mock implementation
        List<PhotoText> photoTexts = new ArrayList<>();
        photoTexts.add(new PhotoText(1L, "myfile.txt", new String[]{"Text 1", "Text 2"}));
        photoTexts.add(new PhotoText(2L, "someList.csv", new String[]{"Text 3", "Text 4"}));
        return photoTexts;
    }

    @GetMapping()
    public List<CollectionDto> gCollections() {
        // todo
        return photoCollectionService.getCollections();
    }


  
}