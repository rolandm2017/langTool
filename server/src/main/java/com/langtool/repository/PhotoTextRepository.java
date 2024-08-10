package com.langtool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.langtool.model.PhotoTextEntity;

import java.util.List;


@Repository
public interface PhotoTextRepository extends JpaRepository<PhotoTextEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO photo_texts (photo_id, text) VALUES (:photoId, :text)", nativeQuery = true)
    void insertPhotoText(Long photoId, String text);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO photo_texts (photo_id, text) VALUES (:photoId, :text) ON CONFLICT DO NOTHING", nativeQuery = true)
    void insertPhotoTextIfNotExists(Long photoId, String text);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO photo_texts (photo_id, text) SELECT :photoId, unnest(:texts)", nativeQuery = true)
    void insertMultiplePhotoTexts(Long photoId, String[] texts);

    @Query(value = "SELECT * FROM photo_texts WHERE photo_id = :photoId", nativeQuery = true)
    List<PhotoTextEntity> findAllByPhotoId(Long photoId);
}
