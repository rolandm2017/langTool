package com.langtool.object;

public class TextGroup {
    private Long id;
    private String fileName;
    private String[] texts;

    public TextGroup(Long id, String fileName, String[] texts) {
        this.id = id;
        this.fileName = fileName;
        this.texts = texts;
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

    public String[] getTexts() {
        return texts;
    }

    public void setTexts(String[] texts) {
        this.texts = texts;
    }
}
