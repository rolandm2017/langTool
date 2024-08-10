package com.langtool.model;

public class PhotoText {
    private Long id;
    private String[] texts;

    public PhotoText(Long id, String[] texts) {
        this.id = id;
        this.texts = texts;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String[] getTexts() {
        return texts;
    }

    public void setTexts(String[] texts) {
        this.texts = texts;
    }
}