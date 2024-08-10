package com.langtool.model;

import jakarta.persistence.*;


@Entity
@Table(name = "photo_texts")
public class PhotoTextEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "photo_id", nullable = false)
    private Long photoId;

    @Column(nullable = false)
    private String text;

    // Constructors
    public PhotoTextEntity() {}

    public PhotoTextEntity(Long photoId, String text) {
        this.photoId = photoId;
        this.text = text;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}