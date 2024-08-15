package com.langtool.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;


@Entity
@Table(name = "words", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"origin"})
})
public class WordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String origin;
    private LocalDateTime dateSubmitted;
    private int novelty;
    private int mentions; // number of times the word has been submitted

    private boolean isKnown;

    @Column(name = "text_group_id", nullable = false)
    private Long collectionId;

    @ManyToMany(mappedBy = "words")
    private Set<TextGroupEntity> textGroups = new HashSet<TextGroupEntity>();

    public WordEntity() {
        //
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setWord(String text) {
        this.origin = text;
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

    public void incrementMentions() {
        this.mentions = this.mentions + 1;
    }

    public void setIsKnown(boolean known) {
        this.isKnown = known;
    }

    public boolean getIsKnown() {
        return isKnown;
    }

     // Getter
     public Set<TextGroupEntity> getTextGroups() {
        return Collections.unmodifiableSet(textGroups);
    }

    // Setter (use with caution)
    protected void setTextGroups(Set<TextGroupEntity> textGroups) {
        this.textGroups = textGroups;
    }

    // Helper methods for maintaining the relationship
    public void addTextGroup(TextGroupEntity textGroup) {
        this.textGroups.add(textGroup);
        textGroup.getWords().add(this);
    }

    public void removeTextGroup(TextGroupEntity textGroup) {
        this.textGroups.remove(textGroup);
        textGroup.getWords().remove(this);
    }
    
}