package com.langtool.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "photos")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filePath;

    // @ElementCollection
    // @CollectionTable(name = "photo_texts", joinColumns = @JoinColumn(name = "photo_id"))
    // @Column(name = "text")
    // private List<String> extractedTexts;

    // Constructors
    public Photo() {}

    public Photo(String filePath, List<String> extractedTexts) {
        this.filePath = filePath;
        // this.extractedTexts = extractedTexts;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    // public List<String> getExtractedTexts() {
    //     return extractedTexts;
    // }

    // public void setExtractedTexts(List<String> extractedTexts) {
    //     this.extractedTexts = extractedTexts;
    // }
}