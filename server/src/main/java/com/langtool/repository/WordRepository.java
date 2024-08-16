package com.langtool.repository;

import com.langtool.model.WordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<WordEntity, Long> {
    Optional<WordEntity> findByOrigin(String origin);

    @Query("SELECT w.mentions FROM WordEntity w WHERE w.origin = :origin")
    Integer findMentionsByOrigin(@Param("origin") String origin);

    @Query("SELECT w FROM WordEntity w WHERE w.origin IN :origins")
    List<WordEntity> findAllByOrigins(@Param("origins") List<String> origins);

    @Modifying
    @Transactional
    @Query(value = "UPDATE word_entity SET mentions = mentions + 1 WHERE origin = ANY(:origins)", nativeQuery = true)
    int incrementMentionsForWords(@Param("origins") String[] origins);
}