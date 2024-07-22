package com.langtool.service;

import com.langtool.dto.WordDto;
import com.langtool.model.Word;
import com.langtool.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WordService {

    @Autowired
    private WordRepository wordRepository;

    public List<WordDto> getAllWords() {
        List<Word> words = wordRepository.findAll();
         List<WordDto> wordDtoList = words.stream()
                                     .map(this::convertWordToDto)
                                     .collect(Collectors.toList());
        return wordDtoList;
    }

    public Word saveWord(WordDto word) {
        Word asEntity = this.convertDtoToWord(word);
        return wordRepository.save(asEntity);
    }

    public Word[] saveWords(WordDto[] words) {
        Word[] savedWords = new Word[words.length];
    
        for (int i = 0; i < words.length; i++) {
            Word word = convertDtoToWord(words[i]);
            savedWords[i] = wordRepository.save(word);
        }
        
        return savedWords;
    }

    public Optional<Word> getWordById(Long id) {
        return wordRepository.findById(id);
    }

    public void deleteWord(Long id) {
        wordRepository.deleteById(id);
    }

    private WordDto convertWordToDto(Word word) {
        WordDto wordDto = new WordDto();
        wordDto.setId(word.getId());
        wordDto.setOrigin(word.getOrigin());
        wordDto.setDateSubmitted(word.getDateSubmitted());
        wordDto.setNovelty(word.getNovelty());
        return wordDto;
    }

    private Word convertDtoToWord(WordDto wordDto) {
        Word word = new Word();
        word.setId(wordDto.getId());
        word.setOrigin(wordDto.getOrigin());
        word.setDateSubmitted(wordDto.getDateSubmitted());
        word.setNovelty(wordDto.getNovelty());
        return word;
    }
    
}
