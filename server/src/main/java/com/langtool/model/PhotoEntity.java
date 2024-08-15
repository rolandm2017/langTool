package com.langtool.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "photos")
public class PhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private LocalDateTime creationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", nullable = true)
    private CollectionEntity collection;

    @OneToOne(mappedBy = "photo", cascade = CascadeType.ALL, orphanRemoval = true)
    private TextGroupEntity textGroup;

    // Constructors
    public PhotoEntity() {}

    public PhotoEntity(String filePath, LocalDateTime creationTime) {
        this.filePath = filePath;
        this.creationTime = creationTime;
        // this.extractedTexts = extractedTexts;
    }

    // Getters and setters
    public Long getId() {
        return photoId;
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

    public CollectionEntity getCollection() {
        return collection;
    }

    public void setCollection(CollectionEntity collection) {
        this.collection = collection;
    }

    // public List<String> getExtractedTexts() {
    //     return extractedTexts;
    // }

    // public void setExtractedTexts(List<String> extractedTexts) {
    //     this.extractedTexts = extractedTexts;
    // }
}