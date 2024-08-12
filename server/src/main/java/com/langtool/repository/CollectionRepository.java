package com.langtool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

import com.langtool.model.CollectionEntity;

@Repository
public interface CollectionRepository extends JpaRepository<CollectionEntity, Long> {


    // Custom query to find collections that contain a specific photo ID
    @Query("SELECT c FROM CollectionEntity c WHERE :photoId MEMBER OF c.photoIds")
    List<CollectionEntity> findByPhotoId(@Param("photoId") Long photoId);

}