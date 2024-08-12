package com.langtool.dto;

import java.util.List;

import java.time.LocalDateTime;

public class CollectionDto {
    private String id;
    private String label;
    private LocalDateTime madeAt;
    private List<PhotoDto> photos;

    // Default constructor
    public CollectionDto() {}

    // Constructor with all fields
    public CollectionDto(String id, String label, LocalDateTime madeAt, List<PhotoDto> photos) {
        this.id = id;
        this.label = label; // label starts as the upload date of the photos
        this.madeAt = madeAt;
        this.photos = photos;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LocalDateTime getMadeAt() {
        return madeAt;
    }

    public void setMadeAt(LocalDateTime madeAt) {
        this.madeAt = madeAt;
    }

    public List<PhotoDto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoDto> photos) {
        this.photos = photos;
    }
}

