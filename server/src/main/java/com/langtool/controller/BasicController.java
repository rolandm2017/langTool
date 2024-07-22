package com.langtool.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class BasicController {

    @PostMapping("/api/data")
    public String postData(@RequestBody WordsRequest wordsRequest) {
        String[] words = wordsRequest.getWords();
        
        System.out.println("Received words: " + Arrays.toString(words));
        
        return "Data received: " + Arrays.toString(words);
    }

    public static class WordsRequest {
        private String[] words;

        public String[] getWords() {
            return words;
        }

        public void setWords(String[] words) {
            this.words = words;
        }
    }
}
