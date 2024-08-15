package com.langtool.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import com.langtool.dto.EntityToDtoConverter;
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
        List<TextGroupDto> dtoList = EntityToDtoConverter.convertTextGroupEntitiesToDtos(all);
        return dtoList;
    }

    
    public void addWordToTextGroup(Long groupId, String word) {
        TextGroupEntity textGroup = textGroupRepository.findById(groupId).orElseThrow();
        WordEntity newWord = new WordEntity();
        newWord.setWord("example");
        textGroup.addWord(newWord);
        textGroupRepository.save(textGroup);  // This will also save the new word
    }

    
}
