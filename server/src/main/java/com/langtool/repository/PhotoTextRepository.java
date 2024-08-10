package com.langtool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import jakarta.persistence.*;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PhotoTextRepository extends JpaRepository<PhotoText, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO photo_texts (photo_id, text) VALUES (:photoId, :text)", nativeQuery = true)
    void insertPhotoText(Long photoId, String text);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO photo_texts (photo_id, text) VALUES (:photoId, :text) ON CONFLICT DO NOTHING", nativeQuery = true)
    void insertPhotoTextIfNotExists(Long photoId, String text);
}

@Entity
@Table(name = "photo_texts")
class PhotoText {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "photo_id", nullable = false)
    private Long photoId;

    @Column(nullable = false)
    private String text;

    // Constructors
    public PhotoText() {}

    public PhotoText(Long photoId, String text) {
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