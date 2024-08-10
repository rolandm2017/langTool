package com.langtool.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class WordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String origin;
    private LocalDateTime dateSubmitted;
    private int novelty;
    private int mentions; // number of times the word has been submitted

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public LocalDateTime getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(LocalDateTime dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public int getNovelty() {
        return novelty;
    }

    public void setNovelty(int novelty) {
        this.novelty = novelty;
    }

    public int getMentions() {
        return mentions;
    }
    
    public void setMentions(int mentions) {
        this.mentions = mentions;
    }
    
}