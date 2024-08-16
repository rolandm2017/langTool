package com.langtool.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.langtool.model.CollectionEntity;
import com.langtool.model.WordEntity;

public class DtoToEntityConverter {
    
    public static WordEntity convertDtoToWord(WordDto wordDto) {
        WordEntity word = new WordEntity();
        word.setId(wordDto.getId());
        word.setOrigin(wordDto.getOrigin());
        word.setDateSubmitted(wordDto.getDateSubmitted());
        word.setNovelty(wordDto.getNovelty());
        return word;
    }

    public static WordEntity[] convertBatchWordDtoToWords(BatchWordDto batchWordDto) {
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
 


}
