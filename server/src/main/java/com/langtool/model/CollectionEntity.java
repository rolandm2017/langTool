package com.langtool.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

@Entity
@Table(name = "collections")
public class CollectionEntity {
    @Id
    // @GeneratedValue(strategy = GenerationType.)
    @Column(name="collectionId")
    private Long collectionId;

    @Column(nullable = true)
    private String label;

    @Column(nullable = true)
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhotoEntity> photos = new ArrayList<>();

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TextGroupEntity> textGroups = new ArrayList<>();

    // Constructors
    public CollectionEntity() {}

    public CollectionEntity(String label, LocalDateTime creationDate) {
        this.label = label;
        this.creationDate = creationDate;
        // this.photoIds = photoIds;
    }

    // Getters and setters
    public Long getId() {
        return collectionId;
    }

    public void setId(Long collectionId) {
        this.collectionId = collectionId;
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


    public List<PhotoEntity> getPhotos() {
        return Collections.unmodifiableList(photos);
    }
    
    protected void setPhotos(List<PhotoEntity> photos) {
        this.photos = photos;
    }
    
    public void addPhoto(PhotoEntity photo) {
        photos.add(photo);
        photo.setCollection(this);
    }

    public void addPhotos(List<PhotoEntity> list) {
        list.forEach(this::addPhoto);
    }
    
    public void removePhoto(PhotoEntity photo) {
        photos.remove(photo);
        photo.setCollection(null);
    }

    public List<TextGroupEntity> getTextGroups() {
        return textGroups;
    }

    @Override
    public String toString() {
        return "CollectionEntity{" +
                "collectionId=" + collectionId +
                ", label='" + label + '\'' +
                ", creationDate=" + creationDate +
                // ", photoIds=" + photoIds +
                '}';
    }

}