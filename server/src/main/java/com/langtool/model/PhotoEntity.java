package com.langtool.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "photos")
public class PhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private LocalDateTime creationTime;

    // TODO: add a "collectionLabel" property.

    // @ElementCollection
    // @CollectionTable(name = "photo_texts", joinColumns = @JoinColumn(name = "photo_id"))
    // @Column(name = "text")
    // private List<String> extractedTexts;

    // Constructors
    public PhotoEntity() {}

    public PhotoEntity(String filePath, LocalDateTime creationTime) {
        this.filePath = filePath;
        this.creationTime = creationTime;
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

    public LocalDateTime getCreationTime() {
        return creationTime;
    }
    
    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    // public List<String> getExtractedTexts() {
    //     return extractedTexts;
    // }

    // public void setExtractedTexts(List<String> extractedTexts) {
    //     this.extractedTexts = extractedTexts;
    // }
}