package com.langtool.service;

import com.langtool.dto.BatchWordDto;
import com.langtool.dto.CsvOutput;
import com.langtool.dto.PhotoDto;
import com.langtool.dto.WordDto;
import com.langtool.model.CollectionEntity;
import com.langtool.model.PhotoEntity;
import com.langtool.model.TextGroupEntity;
import com.langtool.model.WordEntity;
import com.langtool.object.GenericPair;
import com.langtool.repository.CollectionRepository;
import com.langtool.repository.TextGroupRepository;
import com.langtool.repository.WordRepository;

import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class WordsService {

    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private TranslationService translationService;

    @Autowired
    private TextGroupRepository textGroupRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    public List<WordDto> getAllWords() {
        List<WordEntity> words = wordRepository.findAll();
        System.out.println("getAllWords found " + String.valueOf(words.size()));
        List<WordDto> wordDtoList = words.stream()
                                    .map(this::convertWordToDto)
                                    .collect(Collectors.toList());
        System.out.println("dto list size " + String.valueOf(wordDtoList.size()));
        return wordDtoList;
    }

    public List<WordDto> getUnknownWordsFromCollections(List<Long> collectionIds) {
        /*
         * getUnknownWordsFromCollections, as in, "get all the user's collections, and view the words."
         */
        System.out.println("start getUnknownWordsFromCollections 55rm");
        List<WordEntity> wordsFromTextGroups = new ArrayList<>();
        List<WordDto> wordsToLearn = new ArrayList<>();
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
        wordsToLearn = convertWordEntitiesToWordDtos(wordsFromTextGroups);
        System.out.println("are we there yet? 79rm");
        System.out.println("end getUnknownWordsFromCollections 77rm");
        return wordsToLearn;

    }

    private List<WordDto> convertWordEntitiesToWordDtos(List<WordEntity> list) {
        // List<WordDto> dtos = // todo
        // return dtos;
        List<WordDto> wordDtos = new ArrayList<>();
        for (WordEntity word : list) {
            WordDto dto = convertWordEntityToWordDto(word);
            wordDtos.add(dto);
        }
        return wordDtos;
    }

    private WordDto convertWordEntityToWordDto(WordEntity word) {
        return new WordDto(
            // todo
        );
    }

    public WordEntity saveWord(WordDto word) {
        WordEntity asEntity = this.convertDtoToWord(word);
        Integer currentMentions = this.getCurrentMentionCount(asEntity);
        if (currentMentions == null) {
            currentMentions = 0;
        }
        asEntity.setMentions(currentMentions + 1); // set and increment
        return wordRepository.save(asEntity);
    }

    public WordEntity[] saveWords(BatchWordDto words) {
        
        WordEntity[] wordsToSave = this.convertBatchWordDtoToWords(words);
    
        List<WordEntity> savedWords = wordRepository.saveAll(Arrays.asList(wordsToSave));
        
        return savedWords.toArray(new WordEntity[0]); // fixme: does this return the first word or all words?
    }

    public Optional<WordEntity> getWordById(Long id) {
        return wordRepository.findById(id);
    }

    public void deleteWord(Long id) {
        wordRepository.deleteById(id);
    }

    
    private WordEntity convertDtoToWord(WordDto wordDto) {
        WordEntity word = new WordEntity();
        word.setId(wordDto.getId());
        word.setOrigin(wordDto.getOrigin());
        word.setDateSubmitted(wordDto.getDateSubmitted());
        word.setNovelty(wordDto.getNovelty());
        return word;
    }
    
    private Integer getCurrentMentionCount(WordEntity word) {
        return this.wordRepository.findMentionsByOrigin(word.getOrigin());
    }

    private WordEntity[] convertBatchWordDtoToWords(BatchWordDto batchWordDto) {
    return Arrays.stream(batchWordDto.getWords())
        .map(wordString -> {
            WordEntity word = new WordEntity();
            word.setOrigin(wordString);
            word.setDateSubmitted(LocalDateTime.now());
            word.setNovelty(10);  // Default value, adjust as needed
            word.setMentions(1); // Initial submission counts as first mention
            return word;
        })
        .toArray(WordEntity[]::new);
    }

    public CsvOutput generateCsvFrom(BatchWordDto words) {
        // figure out which words are in the db already
        List<WordEntity> existingWords = this.findExistingWords(words);
        // increment the words that are there already by 1
        this.wordRepository.incrementMentionsForWords(existingWords.stream()
                                                        .map(WordEntity::getOrigin)
                                                        .toArray(String[]::new));
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
        CsvOutput csvOutObj = new CsvOutput(csvOut);

        // return the csv
        return csvOutObj;
    }

    public List<WordEntity> findExistingWords(BatchWordDto inputWords) {
        List<String> wordList = Arrays.asList(inputWords.getWords());
        return wordRepository.findAllByOriginIn(wordList);
    }

    public List<WordEntity> findMissingWords(BatchWordDto inputWords) {
        List<String> wordList = Arrays.asList(inputWords.getWords());
        // todo: make this alphabetized for search efficiency
        List<WordEntity> existingWords = wordRepository.findAllByOriginIn(wordList);
        
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
