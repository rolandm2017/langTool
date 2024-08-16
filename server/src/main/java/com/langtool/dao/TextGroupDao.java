package com.langtool.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.langtool.model.PhotoEntity;
import com.langtool.model.TextGroupEntity;
import com.langtool.model.WordEntity;
import com.langtool.repository.TextGroupRepository;
import com.langtool.repository.WordRepository;

@Service
public class TextGroupDao {

    @Autowired
    TextGroupRepository textGroupRepository;

    @Autowired
    WordRepository wordRepository;
    
    public List<TextGroupEntity> findAllTextGroups() {
        return textGroupRepository.findAll();
    }

    public void addWordToTextGroup(Long groupId, String word) {
        TextGroupEntity textGroup = textGroupRepository.findById(groupId).orElseThrow();
        WordEntity newWord = new WordEntity();
        newWord.setWord(word);
        textGroup.addWord(newWord);
        textGroupRepository.save(textGroup);  // This will also save the new word
    }

    public void writeNewTextGroupToDb(Long associatedCollectionId, PhotoEntity newPhotoEntry, List<WordEntity> wordsToAdd) {
        TextGroupEntity toSave = new TextGroupEntity();
        Set<WordEntity> uniqueWordsOnly = new HashSet<WordEntity>();
        Set<Long> seenIds = new HashSet<Long>();
        for (WordEntity word: wordsToAdd) {
            if (seenIds.contains(word.getId())) {
                continue;
            }
            WordEntity afterSave = wordRepository.save(word);
            seenIds.add(afterSave.getId());
            uniqueWordsOnly.add(afterSave);
        }
        toSave.setWords(uniqueWordsOnly);
        toSave.setPhoto(newPhotoEntry);
        System.out.println("268rm");
        System.out.println(toSave.toString());
        System.out.println("my id : ");
        System.out.println(toSave.getId());
        textGroupRepository.save(toSave);
        // Long newPhotoInsertId = newPhotoEntry.getId();
        // textGroupRepository.insertTextGroup(associatedCollectionId, newPhotoInsertId, textArr);
    }

    
}
