package com.langtool.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.langtool.model.CollectionEntity;
import com.langtool.model.PhotoEntity;
import com.langtool.model.TextGroupEntity;
import com.langtool.model.WordEntity;

public class EntityToDtoConverter {

    public static List<WordDto> convertWordEntitiesToWordDtos(List<WordEntity> list) {
        // List<WordDto> dtos = // todo
        // return dtos;
        List<WordDto> wordDtos = new ArrayList<>();
        for (WordEntity word : list) {
            WordDto dto = convertWordEntityToWordDto(word);
            wordDtos.add(dto);
        }
        return wordDtos;
    }

    public static WordDto convertWordEntityToWordDto(WordEntity word) {
        return new WordDto(
            // todo
        );
    }

    public static List<TextGroupDto> convertTextGroupEntitiesToDtos(List<TextGroupEntity> list) {
        List<TextGroupDto> out = new ArrayList<TextGroupDto>();

        for (TextGroupEntity entity: list) {
            String toAdd = ""; 
            Set<WordEntity> wordsToAppend = entity.getWords();
            for (WordEntity word: wordsToAppend) {
                toAdd = toAdd + " " + word.getOrigin();
            }
            TextGroupDto dto = new TextGroupDto(entity.getId(), toAdd);
            out.add(dto);
        }

        return out;
    }

    public static List<TextGroupDto> convertTextGroupEntitiesToTextGroupDtos(List<TextGroupEntity> entities) {
        List<TextGroupDto> out = new ArrayList<>();
        for (TextGroupEntity group: entities) {
            String toAdd = ""; 
            Set<WordEntity> wordsToAppend = group.getWords();
            for (WordEntity word: wordsToAppend) {
                toAdd = toAdd + " " + word.getOrigin();
            }
            TextGroupDto dto = new TextGroupDto(group.getId(), toAdd);
            out.add(dto);
        }
        return out;
    }


    public static List<WordDto> convertWordEntitiesToDtos(List<WordEntity> list) {
        return list.stream()
                   .map(EntityToDtoConverter::convertWordToDto)
                   .collect(Collectors.toList());    
    }

    public static WordDto convertWordToDto(WordEntity word) {
        WordDto wordDto = new WordDto();
        wordDto.setId(word.getId());
        wordDto.setOrigin(word.getOrigin());
        wordDto.setDateSubmitted(word.getDateSubmitted());
        wordDto.setNovelty(word.getNovelty());
        return wordDto;
    }


    public static CollectionDto convertCollectionEntityToDto(CollectionEntity entity) {
        return new CollectionDto(
            String.valueOf(entity.getId()),
            entity.getLabel(),
            entity.getCreationDate(),
            convertPhotoEntitiesToPhotoDtos(entity.getPhotos())
        );
    }

    public static List<PhotoDto> convertPhotoEntitiesToPhotoDtos(List<PhotoEntity> list) {
        List<PhotoDto> photoDtos = new ArrayList<>();
        for (PhotoEntity photo : list) {
            PhotoDto dto = convertPhotoEntityToPhotoDto(photo);
            photoDtos.add(dto);
        }
        return photoDtos;
    }

    public static PhotoDto convertPhotoEntityToPhotoDto(PhotoEntity toConvert) {
        CollectionEntity relatedCollection = toConvert.getCollection();
        List<TextGroupEntity> relatedTextGroup = relatedCollection.getTextGroups();
        Set<WordEntity> wordsToPackage = eliminateDuplicateWords(relatedTextGroup);
        List<String> goIntoHere = makePhotoDtoTextArr(wordsToPackage);
        return new PhotoDto(
            toConvert.getId(),
            toConvert.getFilePath(),
            goIntoHere
        );
    }

    private static Set<WordEntity> eliminateDuplicateWords(List<TextGroupEntity> relatedTextGroup) {
        Set<WordEntity> uniques = new HashSet<WordEntity>();
        Set<Long> seenIds = new HashSet<Long>();
        for (TextGroupEntity tge: relatedTextGroup) {
            Set<WordEntity> words = tge.getWords();
            for (WordEntity w: words) {
                if (seenIds.contains(w.getId())) {
                    continue;
                }
                uniques.add(w);
                seenIds.add(w.getId());
            }
        }
        return uniques;
    }
    
    private static List<String> makePhotoDtoTextArr(Set<WordEntity> words) {
        List<String> goIntoHere = new ArrayList<>();
        for (WordEntity w: words) {
            goIntoHere.add(w.getOrigin());
        }
        return goIntoHere;
    }

    public static List<CollectionDto> convertCollectionEntitiesToDtos(List<CollectionEntity> fromDb) {
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
   
}
