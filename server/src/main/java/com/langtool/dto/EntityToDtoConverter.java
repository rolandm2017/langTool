package com.langtool.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.langtool.model.CollectionEntity;
import com.langtool.model.PhotoEntity;
import com.langtool.model.TextGroupEntity;
import com.langtool.model.WordEntity;
import com.langtool.repository.TextGroupRepository;

public class EntityToDtoConverter {

    private WordDto convertWordToDto(WordEntity word) {
        WordDto wordDto = new WordDto();
        wordDto.setId(word.getId());
        wordDto.setOrigin(word.getOrigin());
        wordDto.setDateSubmitted(word.getDateSubmitted());
        wordDto.setNovelty(word.getNovelty());
        return wordDto;
    }


    private CollectionDto convertCollectionEntityToDto(CollectionEntity entity) {
        return new CollectionDto(
            String.valueOf(entity.getId()),
            entity.getLabel(),
            entity.getCreationDate(),
            convertPhotoEntitiesToPhotoDtos(entity.getPhotos())
        );
    }

    private List<PhotoDto> convertPhotoEntitiesToPhotoDtos(List<PhotoEntity> list) {
        List<PhotoDto> photoDtos = new ArrayList<>();
        for (PhotoEntity photo : list) {
            PhotoDto dto = convertPhotoEntityToPhotoDto(photo);
            photoDtos.add(dto);
        }
        return photoDtos;
    }

    private PhotoDto convertPhotoEntityToPhotoDto(PhotoEntity toConvert) {
        Long id = toConvert.getId();
        System.out.println("processing ... " + Long.toString(id));
        CollectionEntity relatedCollection = toConvert.getCollection();
        List<TextGroupEntity> relatedTextGroup = relatedCollection.getTextGroups();
        Set<WordEntity> wordsCollector = new HashSet<WordEntity>();
        Set<Long> seenIds = new HashSet<Long>();
        for (TextGroupEntity tge: relatedTextGroup) {
            Set<WordEntity> words = tge.getWords();
            for (WordEntity w: words) {
                if (seenIds.contains(w.getId())) {
                    continue;
                }
                wordsCollector.add(w);
                seenIds.add(w.getId());
            }
        }
        List<String> goIntoHere = new ArrayList<>();
        for (WordEntity w: wordsCollector) {
            goIntoHere.add(w.getOrigin());
        }
        return new PhotoDto(
            toConvert.getId(),
            toConvert.getFilePath(),
            goIntoHere
        );
    }
    
    private PhotoDto convertPhotoEntityToPhotoDto(PhotoEntity toConvert, TextGroupRepository textGroupRepository) {
        Optional<TextGroupEntity> photoText = textGroupRepository.findById(toConvert.getId());
        if (photoText.isPresent()) {
            CollectionEntity relatedCollection = toConvert.getCollection();
            List<TextGroupEntity> relatedGroups = relatedCollection.getTextGroups();
            Set<WordEntity> wordsToPackage = new HashSet<>();
            Set<Long> seenIds = new HashSet<>();
            for (TextGroupEntity tg : relatedGroups) {
                Set<WordEntity> words = tg.getWords();
                for (WordEntity w: words) {
                    if (seenIds.contains(w.getId())) {
                        continue;
                    }
                    wordsToPackage.add(w);
                }
            }
            Set<WordEntity> wordsToAdd = wordsToPackage;
            Set<String> justTheirStrings = new HashSet<>();
            for (WordEntity w: wordsToAdd) {
                justTheirStrings.add(w.getOrigin());
            }
            String[] stringArray = justTheirStrings.toArray(new String[0]);
            List<String> wordsToEmbed = Arrays.asList(stringArray);
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
