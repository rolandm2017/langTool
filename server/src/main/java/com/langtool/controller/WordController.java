package com.langtool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.langtool.dto.WordDto;
import com.langtool.dto.BatchWordDto;
import com.langtool.service.WordsService;
import com.langtool.dto.CsvOutput;



@RestController
@RequestMapping("/api/words")
public class WordController {

    @Autowired
    private WordsService wordsService;

    private List<WordDto> words = new ArrayList<>();

    @GetMapping("/all")
    public List<WordDto> getAllWords() {
        return wordsService.getAllWords();
    }

    @PostMapping
    public ResponseEntity<?> addWord(@RequestBody WordDto word) {
        if (originIsNull(word)) {
            ErrorResponse errorResponse = ErrorResponse.builder(
                new IllegalArgumentException("word origin was null"),
                HttpStatus.BAD_REQUEST,
                "Word origin must not be null")
            .title("Invalid Word Data")
            // .type(URI.create("https://api.your-domain.com/errors/invalid-word"))
            .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
        word.setDateSubmitted(LocalDateTime.now());
        // words.add(word);
        this.wordsService.saveWord(word);
        return new ResponseEntity<>(word, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<BatchWordDto> addWords(@RequestBody BatchWordDto words) {
        System.out.println("Number of words submitted: " + words.getWords().length);

        this.wordsService.saveWords(words);
        return new ResponseEntity<BatchWordDto>(words, HttpStatus.CREATED);
    }

    @PostMapping("/generateCsv")
    public ResponseEntity<CsvOutput> generateCsv(@RequestBody BatchWordDto words) {
        System.out.println("Number of words submitted: " + words.getWords().length);

        CsvOutput csv = this.wordsService.generateCsvFrom(words);
        return new ResponseEntity<CsvOutput>(csv, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWord(@PathVariable Long id) {
        wordsService.deleteWord(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private boolean originIsNull(WordDto word) {
        return word.getOrigin() == null;
    }
}