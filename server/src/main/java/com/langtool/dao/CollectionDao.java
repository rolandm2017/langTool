package com.langtool.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.langtool.model.CollectionEntity;
import com.langtool.model.PhotoEntity;
import com.langtool.repository.CollectionRepository;
import com.langtool.repository.PhotoRepository;
import com.langtool.repository.WordRepository;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CollectionDao {

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private PhotoRepository photoRepository;

    public void writeNewCollectionOrUpdate(Long targetCollectionId, LocalDateTime creationTime, PhotoEntity photo) {
        /* Could be the first photo to be uploaded or one of dozens.
         * Hence the Collection might be being created or being updated.<
         * find if this target collection id already exists
         * */
        Optional<CollectionEntity> collectionEntityToUpdate = collectionRepository.findById(targetCollectionId);
        if (collectionEntityToUpdate.isPresent()) {
            System.out.println("add photo to collection: " + targetCollectionId.toString()+ " " + photo.toString());
            CollectionEntity collection = collectionEntityToUpdate.get();
            collection.addPhoto(photo);
            collectionRepository.save(collection);
            photo.setCollection(collection);
            photoRepository.save(photo);
            return;
        }
        // // if it doesn't exist, create a new one with just this one photo id
        CollectionEntity fromScratch = new CollectionEntity();
        fromScratch.addPhoto(photo);
        fromScratch.setCreationDate(creationTime);
        collectionRepository.save(fromScratch);
        photo.setCollection(fromScratch);
        photoRepository.save(photo);
    }

    public List<CollectionEntity> findAllCollections() {
        return collectionRepository.findAll();
    }

    public Optional<CollectionEntity> findByCollectionId(Long id) {
        return collectionRepository.findById(id);
    }

    public Long findHighestId() {
        return collectionRepository.findHighestId();
    }

    
    
    public boolean updateCollectionLabel(Long id, String newLabel) {
        System.out.println("id : " + id + " " + newLabel);
        try {
            CollectionEntity toUpdate = collectionRepository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Collection not found with id: " + id));
            toUpdate.setLabel(newLabel);
            collectionRepository.save(toUpdate);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
