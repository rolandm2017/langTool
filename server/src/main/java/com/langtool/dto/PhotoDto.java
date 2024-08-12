package com.langtool.dto;

import java.util.List;

public class PhotoDto {
    private Long id;
    private String fileName;
    private List<String> words;

    // Default constructor
    public PhotoDto() {}

    // Constructor with all fields
    public PhotoDto(Long id, String fileName, List<String> words) {
        this.id = id;
        this.fileName = fileName;
        this.words = words;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
}