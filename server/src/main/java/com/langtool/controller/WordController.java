package com.langtool.controller;

import com.langtool.dto.WordDto;
import com.langtool.service.WordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/words")
public class WordController {

    @Autowired
    private WordService wordService;

    private List<WordDto> words = new ArrayList<>();

    @GetMapping
    public List<WordDto> getAllWords() {
        return this.wordService.getAllWords();
    }

    @PostMapping
    public ResponseEntity<WordDto> addWord(@RequestBody WordDto word) {
        word.setDateSubmitted(LocalDateTime.now());
        // words.add(word);
        this.wordService.saveWord(word);
        return new ResponseEntity<>(word, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<WordDto[]> addWords(@RequestBody WordDto[] words) {
        this.wordService.saveWords(words);
        return new ResponseEntity<>(words, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWord(@PathVariable Long id) {
        wordService.deleteWord(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}