package com.langtool.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.*;


@Entity
@Table(name = "text_groups")
public class TextGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="textGroupId")

    private Long textGroupId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photoId")
    private PhotoEntity photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collectionId", nullable = true)
    private CollectionEntity collection;

    // @Column(nullable = false, columnDefinition = "TEXT")
    // private String text;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "text_group_words",
        joinColumns = @JoinColumn(name = "text_group_id"),
        inverseJoinColumns = @JoinColumn(name = "word_id")
    )
    private Set<WordEntity> words = new HashSet<WordEntity>();

    // Constructors
    public TextGroupEntity() {}

    public TextGroupEntity(Set<WordEntity> words) { // must be Set<WordEntity> to avoid duplicate words
        // this.photoId = photoId;
        this.words = words;
    }

    // public TextGroupEntity(Long photoId, String text) {
    //     this.photoId = photoId;
    //     this.text = text;
    // }

    // Getters and setters
    public Long getId() {
        return textGroupId;
    }

    public void setId(Long id) {
        textGroupId = id;
    }

    public PhotoEntity getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoEntity photo) {
        this.photo = photo;
    }

    // public Long getCollectionId() {
    //     return collectionId;
    // }

    // public void setCollectionId(Long collectionId) {
    //     this.collectionId = collectionId;
    // }


    public Set<WordEntity> getWords() {
        return words;
    }

    public void setWords(Set<WordEntity> words) {
        this.words = words;
    }

    public void addWords(String[] words) {
        for (String wordString : words) {
            WordEntity word = new WordEntity();
            word.setOrigin(wordString);
            addWord(word);
        }
    }

    public void addWord(WordEntity word) {
        words.add(word);
        word.addTextGroup(this);
    }

    public void removeWord(WordEntity word) {
        words.remove(word);
        word.getTextGroups().remove(this);
    }

    @Override
    public String toString() {
        return "TextGroupEntity{" +
               "textGroupId=" + (textGroupId != null ? textGroupId : "null") +
               ", photo=" + (photo != null ? "PhotoEntity(id=" + photo.getId() + ")" : "null") +
               ", collection=" + (collection != null ? "CollectionEntity(id=" + collection.getId() + ")" : "null") +
               ", words=[" + words.stream()
                               .map(word -> word.getId() + ":" + word.getOrigin())
                               .collect(Collectors.joining(", ")) +
               "]}";
    }
}
