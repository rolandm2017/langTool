package com.langtool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import com.langtool.object.PhotoText;
import com.langtool.repository.PhotoRepository;
import com.langtool.repository.TextGroupRepository;
import com.langtool.repository.CollectionRepository;
import com.langtool.model.CollectionEntity;
import com.langtool.model.PhotoEntity;
import com.langtool.model.TextGroupEntity;

import com.langtool.dto.CollectionDto;
import com.langtool.dto.PhotoDto;

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
            CollectionDto dto = convertCollectionEntityToDto(entity);
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

    public List<PhotoText> getPhotoTexts(Long photoCollectionId) {
        // A photo set is a set of photos. Or a "photo collection".
        // Each entry has associated words.
        // This exchanges the ID of the collection and gets back all the words in each entry.
        Optional<PhotoEntity> photoOptional = photoRepository.findById(photoCollectionId);
        
        if (photoOptional.isPresent()) {
            PhotoEntity photo = photoOptional.get();
            Long photoId = photo.getId();
            List<TextGroupEntity> photoTexts = textGroupRepository.findAllByPhotoId(photoId);
            List<PhotoText> texts = convertEntityToObj(photoTexts, "temp-bandaid-thing");
            return texts;
        }

        return new ArrayList<>(); // Return empty list if photo not found
    }

    private List<PhotoText> convertEntityToObj(List<TextGroupEntity> batch, String fileName) {
        List<PhotoText> created = new ArrayList<>();
        for (TextGroupEntity toConvert: batch) {

            PhotoText converted = new PhotoText(toConvert.getId(), fileName, toConvert.getText().split(" "));
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
            CollectionDto dto = convertCollectionEntityToDto(toConvert);
            dtos.add(dto);
            
            System.out.println("Processing item at index: " + i);
            i++; 
        }
        return dtos;
    }

    private CollectionDto convertCollectionEntityToDto(CollectionEntity entity) {
        return new CollectionDto(
            String.valueOf(entity.getId()),
            entity.getLabel(),
            entity.getCreationDate(),
            convertPhotoIdsToPhotoDtos(entity.getPhotoIds())
        );
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
        Optional<TextGroupEntity> photoText = textGroupRepository.findById(toConvert.getId());
        if (photoText.isPresent()) {
            String[] splitText = photoText.get().getText().split(" ");
            List<String> wordsToEmbed = Arrays.asList(splitText);
            return new PhotoDto(
                toConvert.getId(),
                toConvert.getFilePath(),
                wordsToEmbed
            );
        }
        out("something went wrong with " + toConvert.getId().toString());
        List<String> failure = new ArrayList<>();
        Long failureValue = null;
        return new PhotoDto(failureValue, "Error", failure);
        
    }

    private void out(String t) {
        System.out.println(t);
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