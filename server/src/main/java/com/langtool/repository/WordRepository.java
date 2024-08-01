package com.langtool.repository;

import com.langtool.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {
    Word findByOrigin(String origin);

    @Query("SELECT w.mentions FROM Word w WHERE w.origin = :origin")
    Integer findMentionsByOrigin(@Param("origin") String origin);

    @Query("SELECT w FROM Word w WHERE w.origin IN :origins")
    List<Word> findAllByOriginIn(@Param("origins") List<String> origins);

    @Modifying
    @Transactional
    @Query(value = "UPDATE word SET mentions = mentions + 1 WHERE origin = ANY(:origins)", nativeQuery = true)
    int incrementMentionsForWords(@Param("origins") String[] origins);
}