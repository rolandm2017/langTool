package com.langtool.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "collections")
public class CollectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @ElementCollection
    @CollectionTable(name = "collection_photos", joinColumns = @JoinColumn(name = "collection_id"))
    @Column(name = "photo_id")
    private List<Long> photoIds;

    // Constructors
    public CollectionEntity() {}

    public CollectionEntity(String label, LocalDateTime creationDate, List<Long> photoIds) {
        this.label = label;
        this.creationDate = creationDate;
        this.photoIds = photoIds;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public List<Long> getPhotoIds() {
        return photoIds;
    }

    public void setPhotoIds(List<Long> photoIds) {
        this.photoIds = photoIds;
    }
}