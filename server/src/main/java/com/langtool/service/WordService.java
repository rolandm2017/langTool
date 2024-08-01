package com.langtool.service;

import com.langtool.dto.BatchWordDto;
import com.langtool.dto.CsvOutput;
import com.langtool.dto.WordDto;
import com.langtool.model.Word;
import com.langtool.object.GenericPair;
import com.langtool.repository.WordRepository;

import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class WordService {

    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private TranslationService translationService;

    public List<WordDto> getAllWords() {
        List<Word> words = wordRepository.findAll();
         List<WordDto> wordDtoList = words.stream()
                                     .map(this::convertWordToDto)
                                     .collect(Collectors.toList());
        return wordDtoList;
    }

    public Word saveWord(WordDto word) {
        Word asEntity = this.convertDtoToWord(word);
        Integer currentMentions = this.getCurrentMentionCount(asEntity);
        if (currentMentions == null) {
            currentMentions = 0;
        }
        asEntity.setMentions(currentMentions + 1); // set and increment
        return wordRepository.save(asEntity);
    }

    public Word[] saveWords(BatchWordDto words) {
        
        Word[] wordsToSave = this.convertBatchWordDtoToWords(words);
    
        List<Word> savedWords = wordRepository.saveAll(Arrays.asList(wordsToSave));
        
        return savedWords.toArray(new Word[0]); // fixme: does this return the first word or all words?
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
    
    private Integer getCurrentMentionCount(Word word) {
        return this.wordRepository.findMentionsByOrigin(word.getOrigin());
    }

    private Word[] convertBatchWordDtoToWords(BatchWordDto batchWordDto) {
    return Arrays.stream(batchWordDto.getWords())
        .map(wordString -> {
            Word word = new Word();
            word.setOrigin(wordString);
            word.setDateSubmitted(LocalDateTime.now());
            word.setNovelty(10);  // Default value, adjust as needed
            word.setMentions(1); // Initial submission counts as first mention
            return word;
        })
        .toArray(Word[]::new);
    }

    public CsvOutput generateCsvFrom(BatchWordDto words) {
        // figure out which words are in the db already
        List<Word> existingWords = this.findExistingWords(words);
        // increment the words that are there already by 1
        this.wordRepository.incrementMentionsForWords(existingWords.stream()
                                                        .map(Word::getOrigin)
                                                        .toArray(String[]::new));
        // add the words that aren't in the db yet
        List<Word> novelWords = this.findMissingWords(words);

        // translate the novel words
        List<GenericPair> translatedPairs = new ArrayList<>();
            // todo - for word in novelWords, translate Fr->En
        for (Word w: novelWords) {
            String fr = w.getOrigin();
            String en = this.translationService.translateFrToEn(fr);
            GenericPair paired = new GenericPair(en, fr);
            translatedPairs.add(paired);
        }
        // get the csv
        String csvOut = "";
        for (GenericPair pair: translatedPairs) {
            String singleLine = pair.getEn() + "," + pair.getFr();
            csvOut = csvOut + singleLine + "\n";
        }
        CsvOutput csvOutObj = new CsvOutput(csvOut);

        // return the csv
        return csvOutObj;
    }

    public List<Word> findExistingWords(BatchWordDto inputWords) {
        List<String> wordList = Arrays.asList(inputWords.getWords());
        return wordRepository.findAllByOriginIn(wordList);
    }

    public List<Word> findMissingWords(BatchWordDto inputWords) {
        List<String> wordList = Arrays.asList(inputWords.getWords());
        // todo: make this alphabetized for search efficiency
        List<Word> existingWords = wordRepository.findAllByOriginIn(wordList);
        
        List<Word> missingWords = new ArrayList<>();
        for (Word wordToCheck: existingWords) {
            // fixme: is this optimized? best way to search for a text in an alphabetized string arr?
            if (wordList.contains(wordToCheck.getOrigin())) {
                continue;
            }
            missingWords.add(wordToCheck);
        }
        return missingWords;
    }
}
