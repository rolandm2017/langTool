package com.langtool.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.langtool.model.WordEntity;
import com.langtool.repository.WordRepository;

@Service
public class WordDao {


    @Autowired
    private WordRepository wordRepository;

    public Optional<WordEntity> findWordByOrigin(String origin) {
        return wordRepository.findByOrigin(origin);
    }

    public List<WordEntity> findAllWords() {
        return wordRepository.findAll();
    }
        
    public Optional<WordEntity> findWordById(Long id) {
        return wordRepository.findById(id);
    }

    public List<WordEntity> findAllByOrigins(List<String> wordList) {
        return wordRepository.findAllByOrigins(wordList);
    }

    public Integer findMentionsByOrigin(String origin) {
        return wordRepository.findMentionsByOrigin(origin);
    }

    public void incrementMentionsForWords(List<WordEntity> wordsToIncrement) {
        String[] originsArr = wordsToIncrement.stream()
                                .map(WordEntity::getOrigin)
                                .toArray(String[]::new);
        wordRepository.incrementMentionsForWords(originsArr);
    }

    public void saveWord(WordEntity word) {
        wordRepository.save(word);
    }

    public List<WordEntity> saveAllWords(WordEntity[] wordsToSave) {
        return wordRepository.saveAll(Arrays.asList(wordsToSave));
    }



    public void deleteWordById(Long id) {
        wordRepository.deleteById(id);
    }


        

}
