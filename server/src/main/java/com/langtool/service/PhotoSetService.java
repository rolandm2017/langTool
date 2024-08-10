package com.langtool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Optional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.langtool.object.PhotoText;
import com.langtool.repository.PhotoSetRepository;

import com.langtool.model.Photo;

@Service
class PhotoSetService {

    @Autowired
    private PhotoSetRepository photoSetRepository;

    public List<PhotoText> getPhotoTexts(Long photoSetId) {
        Optional<Photo> photoOptional = photoSetRepository.findById(photoSetId);
        
        if (photoOptional.isPresent()) {
            Photo photo = photoOptional.get();
            return photo.getExtractedTexts().stream()
                .map(text -> new PhotoText(photo.getId(), new String[]{text}))
                .collect(Collectors.toList());
        }

        return new ArrayList<>(); // Return empty list if photo not found
    }
}