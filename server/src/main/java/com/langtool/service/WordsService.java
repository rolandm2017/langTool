package com.langtool.service;

import com.langtool.dao.WordDao;
import com.langtool.dto.BatchWordDto;
import com.langtool.dto.CsvOutputDto;
import com.langtool.dto.DtoToEntityConverter;
import com.langtool.dto.EntityToDtoConverter;
import com.langtool.dto.WordDto;
import com.langtool.model.TextGroupEntity;
import com.langtool.model.WordEntity;
import com.langtool.object.GenericPair;
import com.langtool.repository.CollectionRepository;
import com.langtool.repository.TextGroupRepository;
import com.langtool.repository.WordRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Optional;

@Service
public class WordsService {

    @Autowired
    private TranslationService translationService;

    @Autowired
    private TextGroupRepository textGroupRepository;

    @Autowired
    private WordDao wordDao;

    public List<WordDto> getAllWords() {
        List<WordEntity> words = wordDao.findAllWords();
        System.out.println("getAllWords found " + String.valueOf(words.size()));
        List<WordDto> wordDtoList = EntityToDtoConverter.convertWordEntitiesToDtos(words);
        System.out.println("dto list size " + String.valueOf(wordDtoList.size()));
        return wordDtoList;
    }

    public List<WordDto> getUnknownWordsFromCollections(List<Long> collectionIds) {
        /*
         * getUnknownWordsFromCollections, as in, "get all the user's collections, and view the words."
         */
        System.out.println("start getUnknownWordsFromCollections 55rm");
        List<WordEntity> wordsFromTextGroups = getAllWordsFromTextGroups(collectionIds);
        List<WordDto> wordsToLearn = EntityToDtoConverter.convertWordEntitiesToWordDtos(wordsFromTextGroups);
        System.out.println("are we there yet? 79rm");
        System.out.println("end getUnknownWordsFromCollections 77rm");
        return wordsToLearn;

    }

    private List<WordEntity> getAllWordsFromTextGroups(List<Long> collectionIds) {
        List<WordEntity> wordsFromTextGroups = new ArrayList<>();
        Set<Long> alreadySeenWordsIds = new HashSet<Long>();
        for (Long someCollectionId: collectionIds) {
            List<TextGroupEntity> groups = textGroupRepository.findByCollectionId(someCollectionId);
            for (TextGroupEntity group: groups) {
                Set<WordEntity> wordsFromGroup = group.getWords();
                for (WordEntity word: wordsFromGroup) {
                    Long wordId = word.getId();
                    boolean wordIsAlreadyInOutput = alreadySeenWordsIds.contains(wordId);
                    if (wordIsAlreadyInOutput) {
                        continue; // don't get the same word twice
                    }
                    wordsFromTextGroups.add(word);
                    alreadySeenWordsIds.add(wordId);

                }
            }
        }
        return wordsFromTextGroups;
    }



    public void saveWord(WordDto word) {
        WordEntity asEntity = DtoToEntityConverter.convertDtoToWord(word);
        Integer currentMentions = this.getCurrentMentionCount(asEntity);
        if (currentMentions == null) {
            currentMentions = 0;
        }
        asEntity.setMentions(currentMentions + 1); // set and increment
        wordDao.saveWord(asEntity);
    }

    public WordEntity[] saveWords(BatchWordDto words) {
        
        WordEntity[] wordsToSave = DtoToEntityConverter.convertBatchWordDtoToWords(words);
    
        List<WordEntity> savedWords = wordDao.saveAllWords(wordsToSave);
        
        return savedWords.toArray(new WordEntity[0]); // fixme: does this return the first word or all words?
    }

    public Optional<WordEntity> getWordById(Long id) {
        return wordDao.findWordById(id);
    }

    public void deleteWord(Long id) {
        wordDao.deleteWordById(id);
    }

    

    
    private Integer getCurrentMentionCount(WordEntity word) {
        return this.wordDao.findMentionsByOrigin(word.getOrigin());
    }

   

    public CsvOutputDto generateCsvFrom(BatchWordDto words) {
        // figure out which words are in the db already
        List<WordEntity> existingWords = this.findExistingWords(words);
        // increment the words that are there already by 1
        wordDao.incrementMentionsForWords(existingWords);
        // add the words that aren't in the db yet
        List<WordEntity> novelWords = this.findMissingWords(words);

        // translate the novel words
        List<GenericPair> translatedPairs = new ArrayList<>();
            // todo - for word in novelWords, translate Fr->En
        for (WordEntity w: novelWords) {
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
        CsvOutputDto csvOutObj = new CsvOutputDto(csvOut);

        // return the csv
        return csvOutObj;
    }

    public List<WordEntity> findExistingWords(BatchWordDto inputWords) {
        List<String> wordList = Arrays.asList(inputWords.getWords());
        return wordDao.findAllByOrigins(wordList);
    }

    public List<WordEntity> findMissingWords(BatchWordDto inputWords) {
        List<String> wordList = Arrays.asList(inputWords.getWords());
        // todo: make this alphabetized for search efficiency
        List<WordEntity> existingWords = wordDao.findAllByOrigins(wordList);
        
        List<WordEntity> missingWords = new ArrayList<>();
        for (WordEntity wordToCheck: existingWords) {
            // fixme: is this optimized? best way to search for a text in an alphabetized string arr?
            if (wordList.contains(wordToCheck.getOrigin())) {
                continue;
            }
            missingWords.add(wordToCheck);
        }
        return missingWords;
    }
}
