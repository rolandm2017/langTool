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
import com.langtool.repository.PhotoRepository;
import com.langtool.repository.PhotoTextRepository;
import com.langtool.model.PhotoEntity;
import com.langtool.model.PhotoTextEntity;

@Service
class PhotoCollectionService {

    @Autowired
    private PhotoRepository photoCollectionRepository;
    @Autowired
    private PhotoTextRepository photoTextRepository;

    public List<PhotoText> getPhotoTexts(Long photoCollectionId) {
        // A photo set is a set of photos. Or a "photo collection".
        // Each entry has associated words.
        // This exchanges the ID of the collection and gets back all the words in each entry.
        Optional<PhotoEntity> photoOptional = photoCollectionRepository.findById(photoCollectionId);
        
        if (photoOptional.isPresent()) {
            PhotoEntity photo = photoOptional.get();
            Long photoId = photo.getId();
            List<PhotoTextEntity> photoTexts = photoTextRepository.findAllByPhotoId(photoId);
            List<PhotoText> texts = convertEntityToObj(photoTexts);
            return texts;
            // return photo.getExtractedTexts().stream()
            //     .map(text -> new PhotoText(photo.getId(), new String[]{text}))
            //     .collect(Collectors.toList());
        }

        return new ArrayList<>(); // Return empty list if photo not found
    }

    private List<PhotoText> convertEntityToObj(List<PhotoTextEntity> batch) {
        List<PhotoText> created = new ArrayList<>();
        for (PhotoTextEntity toConvert: batch) {
            PhotoText converted = new PhotoText(toConvert.getId(), toConvert.getText().split(" "));
            created.add(converted);
        }
        return created;
    }
}