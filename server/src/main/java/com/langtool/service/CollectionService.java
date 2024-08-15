package com.langtool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import com.langtool.object.TextGroup;
import com.langtool.repository.PhotoRepository;
import com.langtool.repository.TextGroupRepository;

import jakarta.persistence.EntityNotFoundException;

import com.langtool.repository.CollectionRepository;
import com.langtool.model.CollectionEntity;
import com.langtool.model.PhotoEntity;
import com.langtool.model.TextGroupEntity;
import com.langtool.model.WordEntity;

import com.langtool.dto.CollectionDto;
import com.langtool.dto.EntityToDtoConverter;
import com.langtool.dto.PhotoDto;

import java.util.HashSet;;

@Service
public class CollectionService {

    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private TextGroupRepository textGroupRepository;

    public CollectionDto getCollectionById(Long id) {
        Optional<CollectionEntity> maybeEntity = collectionRepository.findById(id);

        if (maybeEntity.isPresent()) {
            CollectionEntity entity = maybeEntity.get();
            CollectionDto dto = EntityToDtoConverter.convertCollectionEntityToDto(entity);
            return dto;
        }
        return new CollectionDto();
    }

    public Long getHighestCollectionId() {
        Long highest = collectionRepository.findHighestId();
        System.out.println("Highest collection ID: " + String.valueOf(highest));
        boolean absolutelyNoRows = highest == null;
        if (absolutelyNoRows) {
            return null;
        }
        System.out.println("45rm");
        System.out.println(highest);
        return highest;
    }

   

    // private void writeNewEmptyRow(Long forId, int totalExpectedFilesNum) {
    //     collectionRepository.writeContainerRow(forId, Long.valueOf(totalExpectedFilesNum));
    // }

    public List<TextGroup> getPhotoTexts(Long photoCollectionId) {
        // A photo set is a set of photos. Or a "photo collection".
        // Each entry has associated words.
        // This exchanges the ID of the collection and gets back all the words in each entry.
        Optional<PhotoEntity> photoOptional = photoRepository.findById(photoCollectionId);
        
        if (photoOptional.isPresent()) {
            PhotoEntity photo = photoOptional.get();
            Long photoId = photo.getId();
            List<TextGroupEntity> photoTexts = textGroupRepository.findAllByPhotoId(photoId);
            List<TextGroup> texts = convertEntityToObj(photoTexts, "temp-bandaid-thing");
            return texts;
        }

        return new ArrayList<>(); // Return empty list if photo not found
    }

    private List<TextGroup> convertEntityToObj(List<TextGroupEntity> batch, String fileName) {
        List<TextGroup> created = new ArrayList<>();
        for (TextGroupEntity toConvert: batch) {

            Set<WordEntity> wordsToAdd = toConvert.getWords();
            Set<String> justTheirStrings = new HashSet<>();
            for (WordEntity w: wordsToAdd) {
                justTheirStrings.add(w.getOrigin());
            }

            String[] stringArray = justTheirStrings.toArray(new String[0]);

            TextGroup converted = new TextGroup(toConvert.getId(), fileName, stringArray);
            created.add(converted);
        }
        return created;
    }

    public List<CollectionDto> getCollections() {
        List<CollectionEntity> allCollections = collectionRepository.findAll();
        System.out.println("Number of collections: " + allCollections.size());
        List<CollectionDto> forTransfer = convertCollectionEntitiesToDtos(allCollections);
        return forTransfer;
       
    }

    private List<CollectionDto> convertCollectionEntitiesToDtos(List<CollectionEntity> fromDb) {
        List<CollectionDto> dtos = new ArrayList<>();
        int i = 0;

        for (CollectionEntity toConvert : fromDb) {
            CollectionDto dto = EntityToDtoConverter.convertCollectionEntityToDto(toConvert);
            dtos.add(dto);
            
            System.out.println("Processing item at index: " + i);
            i++; 
        }
        return dtos;
    }





    private List<PhotoDto> convertPhotoIdsToPhotoDtos(List<Long> photoIds) {
        List<PhotoDto> photoDtos = new ArrayList<>();
        for (Long photoId : photoIds) {
            Optional<PhotoEntity> photo = photoRepository.findById(photoId);
            if (photo.isPresent()){
                PhotoDto dto = EntityToDtoConverter.convertPhotoEntityToPhotoDto(photo.get());
                photoDtos.add(dto);
            }
            
        }
        return photoDtos;
    }

   
    private void out(String t) {
        System.out.println(t);
    }

    public boolean updateCollectionLabel(String id, String newLabel) {
        System.out.println("id : " + id + " " + newLabel);
        try {
            Long idAsLong = Long.valueOf(id);
            CollectionEntity toUpdate = collectionRepository.findById(idAsLong)
            .orElseThrow(() -> new EntityNotFoundException("Collection not found with id: " + id));
            toUpdate.setLabel(newLabel);
            collectionRepository.save(toUpdate);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // public Long useIdToExpectFiles(int totalExpectedFiles) {
    //     /*
    //      * Marked YAGNI
    //      */
    //     // write t rows into the collections.
    //     // will continue elsewhere like this: "if (remainingContainerRowsForId(someId)) { fillContainerRow(fileName)}"
    //     Long highest = getHighestCollectionId();
    //     Long nextCollectionId = highest + 1;
    //     writeNewEmptyRow(nextCollectionId, totalExpectedFiles);
    //     return highest;
    // }
}