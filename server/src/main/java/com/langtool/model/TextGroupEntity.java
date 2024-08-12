package com.langtool.model;

import jakarta.persistence.*;


@Entity
@Table(name = "text_groups")
public class TextGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "photo_id", nullable = false)
    private Long photoId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    // Constructors
    public TextGroupEntity() {}

    public TextGroupEntity(Long photoId, String text) {
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