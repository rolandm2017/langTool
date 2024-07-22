package com.langtool.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WordDto {
    private Long id;
    private String origin;
    private LocalDateTime dateSubmitted;
    private int novelty;

    // Constructor
    public WordDto() {}

    public WordDto(Long id, String origin, LocalDateTime dateSubmitted, int novelty) {
        this.id = id;
        this.origin = origin;
        this.dateSubmitted = dateSubmitted;
        this.novelty = novelty;
    }

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
}
