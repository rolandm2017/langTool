package com.langtool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.langtool.model.TextGroupEntity;

import java.util.List;


@Repository
public interface TextGroupRepository extends JpaRepository<TextGroupEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO text_groups (photo_id, text) VALUES (:photoId, :text)", nativeQuery = true)
    void insertTextGroup(Long photoId, String text);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO text_groups (photo_id, text) VALUES (:photoId, :text) ON CONFLICT DO NOTHING", nativeQuery = true)
    void insertTextGroupIfNotExists(Long photoId, String text);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO text_groups (photo_id, text) SELECT :photoId, unnest(:texts)", nativeQuery = true)
    void insertMultipleTextGroups(Long photoId, String[] texts);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO text_groups (photo_id, text) SELECT :photoId, unnest(:texts)", nativeQuery = true)
    void insertTextGroup(Long photoId, String[] texts);

    @Query(value = "SELECT * FROM text_groups WHERE photo_id = :photoId", nativeQuery = true)
    List<TextGroupEntity> findAllByPhotoId(Long photoId);
}
