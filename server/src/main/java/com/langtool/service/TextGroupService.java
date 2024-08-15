package com.langtool.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.langtool.dto.TextGroupDto;
import com.langtool.model.TextGroupEntity;
import com.langtool.model.WordEntity;
import com.langtool.repository.TextGroupRepository;


@Service
public class TextGroupService {
    
    @Autowired
    TextGroupRepository textGroupRepository;

    public List<TextGroupDto> getAllTextGroups() {
        List<TextGroupEntity> all = textGroupRepository.findAll();
        System.out.println("found " +all.size() + " items");
        List<TextGroupDto> dtoList = convertTextGroupEntitiesToDtos(all);
        return dtoList;
    }

    private List<TextGroupDto> convertTextGroupEntitiesToTextGroupDtos(List<TextGroupEntity> entities) {
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

    public void addWordToTextGroup(Long groupId, String word) {
        TextGroupEntity textGroup = textGroupRepository.findById(groupId).orElseThrow();
        WordEntity newWord = new WordEntity();
        newWord.setWord("example");
        textGroup.addWord(newWord);
        textGroupRepository.save(textGroup);  // This will also save the new word
    }

    public List<TextGroupDto> convertTextGroupEntitiesToDtos(List<TextGroupEntity> list) {
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
}
