package com.langtool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.langtool.object.PhotoText;
import com.langtool.object.Photo;
import com.langtool.repository.PhotoRepository;
import com.langtool.repository.PhotoTextRepository;
import com.langtool.repository.CollectionRepository;
import com.langtool.model.CollectionEntity;
import com.langtool.model.PhotoEntity;
import com.langtool.model.PhotoTextEntity;

import com.langtool.dto.CollectionDto;
import com.langtool.dto.PhotoDto;

@Service
public class PhotoCollectionService {

    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private PhotoTextRepository photoTextRepository;

    public List<PhotoText> getPhotoTexts(Long photoCollectionId) {
        // A photo set is a set of photos. Or a "photo collection".
        // Each entry has associated words.
        // This exchanges the ID of the collection and gets back all the words in each entry.
        Optional<PhotoEntity> photoOptional = photoRepository.findById(photoCollectionId);
        
        if (photoOptional.isPresent()) {
            PhotoEntity photo = photoOptional.get();
            Long photoId = photo.getId();
            List<PhotoTextEntity> photoTexts = photoTextRepository.findAllByPhotoId(photoId);
            List<PhotoText> texts = convertEntityToObj(photoTexts, "temp-bandaid-thing");
            return texts;
            // return photo.getExtractedTexts().stream()
            //     .map(text -> new PhotoText(photo.getId(), new String[]{text}))
            //     .collect(Collectors.toList());
        }

        return new ArrayList<>(); // Return empty list if photo not found
    }

    private List<PhotoText> convertEntityToObj(List<PhotoTextEntity> batch, String fileName) {
        List<PhotoText> created = new ArrayList<>();
        for (PhotoTextEntity toConvert: batch) {

            PhotoText converted = new PhotoText(toConvert.getId(), fileName, toConvert.getText().split(" "));
            created.add(converted);
        }
        return created;
    }

    public List<CollectionDto> getCollections() {
        List<CollectionEntity> allCollections = collectionRepository.findAll();
        List<CollectionDto> forTransfer = convertCollectionEntitiesToDtos(allCollections);
        return forTransfer;
       
    }

    private List<CollectionDto> convertCollectionEntitiesToDtos(List<CollectionEntity> fromDb) {
        List<CollectionDto> dtos = new ArrayList<>();
        for (CollectionEntity toConvert : fromDb) {
            CollectionDto dto = new CollectionDto(
                String.valueOf(toConvert.getId()),
                toConvert.getLabel(),
                toConvert.getCreationDate(),
                convertPhotoIdsToPhotoDtos(toConvert.getPhotoIds())
            );
            dtos.add(dto);
        }
        return dtos;
    }

    private List<PhotoDto> convertPhotoIdsToPhotoDtos(List<Long> photoIds) {
        List<PhotoDto> photoDtos = new ArrayList<>();
        for (Long photoId : photoIds) {
            Optional<PhotoEntity> photo = photoRepository.findById(photoId);
            if (photo.isPresent()){
                PhotoDto dto = convertPhotoEntityToPhotoDto(photo.get());
                photoDtos.add(dto);
            }
            
        }
        return photoDtos;
    }

    private PhotoDto convertPhotoEntityToPhotoDto(PhotoEntity toConvert) {
        Optional<PhotoTextEntity> photoText = photoTextRepository.findById(toConvert.getId());
        if (photoText.isPresent()) {
            String[] splitText = photoText.get().getText().split(" ");
            List<String> wordsToEmbed = Arrays.asList(splitText);
            return new PhotoDto(
                toConvert.getId(),
                toConvert.getFilePath(),
                wordsToEmbed
            );
        }
        List<String> failure = new ArrayList<>();
        Long failureValue = null;
        return new PhotoDto(failureValue, "Error", failure);
        
    }
}