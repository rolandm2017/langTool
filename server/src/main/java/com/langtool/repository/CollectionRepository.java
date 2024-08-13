package com.langtool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

import com.langtool.model.CollectionEntity;

import jakarta.persistence.EntityNotFoundException;

@Repository
public interface CollectionRepository extends JpaRepository<CollectionEntity, Long> {


    @Modifying
    @Transactional
    @Query("UPDATE CollectionEntity c SET c.photoIds = COALESCE(c.photoIds, :emptyList) WHERE c.collectionId = :collectionId")
    void initializePhotoIdsList(@Param("collectionId") Long collectionId, @Param("emptyList") List<Long> emptyList);

    
    // Custom query to find collections that contain a specific photo ID
    @Query("SELECT c FROM CollectionEntity c WHERE :photoId MEMBER OF c.photoIds")
    List<CollectionEntity> findByPhotoId(@Param("photoId") Long photoId);

    // Custom query to get the highest ID in the table
    @Query("SELECT MAX(c.collectionId) FROM CollectionEntity c")
    Long findHighestId();

    @Query("SELECT COUNT(c) > 0 FROM CollectionEntity c WHERE c.collectionId = :id")
    boolean existsById(@Param("id") Long id);


    // @Transactional
    default CollectionEntity insertNewCollectionWithOnePhoto(Long newCollectionId, LocalDateTime creationTime, Long starterPhotoId) {
        System.out.println("+++ 43rm");
        String starterLabel = creationTime.toString();
        CollectionEntity newCollection = new CollectionEntity();
        newCollection.setId(newCollectionId);
        save(newCollection);
        flush();
        
        System.out.println("++++++ 48rm");
        CollectionEntity toUpdate = findById(newCollectionId).orElseThrow(() -> new EntityNotFoundException("Collection not found"));;
        System.out.println(toUpdate);
        System.out.println("+++=========== 51rm");
        toUpdate.setLabel(starterLabel);
        System.out.println("+++=========== 52rm");
        // toUpdate.setCreationDate(creationTime);
        System.out.println("+++=========== 53rm");
        List<Long> photoList = new ArrayList<>();
        photoList.add(starterPhotoId);
        System.out.println(photoList);
        // toUpdate.setPhotoIds(photoList);
        System.out.println("+++=========== 54rm");
        toUpdate.setCreationDate(creationTime);
        CollectionEntity saved = save(toUpdate);
        flush();
        System.out.println("=======================\n=========\n=========== 65rm");
        // return newCollection;
        return saved;
    }
    
    @Transactional
    default void writeAdditionalPhotoIdToCollection(Long collectionId, Long photoId) throws EntityNotFoundException {
        List<CollectionEntity> all = findAll();
        System.out.println("All Collection IDs: " + all.stream().map(CollectionEntity::getId).collect(Collectors.toList()));
        System.out.println("my collection ID: " + String.valueOf(collectionId));
        CollectionEntity collection = findById(collectionId)
                .orElseThrow(() -> new EntityNotFoundException("Collection not found"));
        System.out.println(String.valueOf(collection.getId()) + " gains photo with id: " + photoId.toString());
        List<Long> photoIds = collection.getPhotoIds();
        if (photoIds == null) {
            photoIds = new ArrayList<>();
        }
        photoIds.add(photoId);
        collection.setPhotoIds(photoIds);
        System.out.println("+++++++++ adding new photo +++++++++++++ 76rm");
        System.out.println(collection.toString());
        save(collection);
        flush();
        System.out.println("    _    /\\                  ~       /\\");
        System.out.println("  _|_|_  /  \\   __    _____     ____/  \\");
        System.out.println(" |_____|/____\\ |__|  |_____| O |_______/");
    }

    // // Method to write a container row with only id
    // default CollectionEntity writeContainerRow(Long collectionId, Long expectedNumberOfFiles) {
    //     CollectionEntity containerRow = new CollectionEntity();
    //     containerRow.setId(collectionId);
    //     containerRow.setRemainingExpectedFiles(expectedNumberOfFiles);
    //     return save(containerRow);
    // }

    // Nonsense: There would only ever be one as the Collection is a row with an array of IDs.
    // @Query("SELECT COUNT(c) FROM CollectionEntity c WHERE c.id = :id AND c.label IS NULL AND c.creationDate IS NULL AND (c.photoIds IS NULL OR c.photoIds IS EMPTY)")
    // int countRemainingContainerRowsForId(@Param("id") Long collectionId);

  

    // FIXME: Should be "find the row where remainingExpectedFiles != 0"
    @Query("SELECT c.collectionId FROM CollectionEntity c WHERE c.label IS NULL AND c.creationDate IS NULL AND (c.photoIds IS NULL OR c.photoIds IS EMPTY)")
    List<Long> findRemainingContainerRowIds();

    // Method to get all container rows
    @Query("SELECT c FROM CollectionEntity c WHERE c.label IS NULL AND c.creationDate IS NULL AND c.photoIds IS EMPTY")
    List<CollectionEntity> getAllContainerRows();
}