package com.langtool.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

import com.langtool.object.PhotoText;

@RestController
@RequestMapping("/api/photosets")
public class PhotoSetController {

    @GetMapping("/{photoSetId}")
    public List<PhotoText> getPhotoTexts(@PathVariable Long photoSetId) {
        // TODO: Implement the logic to fetch photo texts based on the photoSetId
        // This is a mock implementation
        List<PhotoText> photoTexts = new ArrayList<>();
        photoTexts.add(new PhotoText(1L, new String[]{"Text 1", "Text 2"}));
        photoTexts.add(new PhotoText(2L, new String[]{"Text 3", "Text 4"}));
        return photoTexts;
    }

  
}