package com.langtool.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.langtool.model.CollectionEntity;
import com.langtool.model.PhotoEntity;
import com.langtool.repository.CollectionRepository;
import com.langtool.repository.PhotoRepository;

@Service
public class PhotoDao {
    
    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    public PhotoEntity writeNewPhotoToDb(String photoFileName, LocalDateTime creationTime, Long intendedCollectionId) {
        List<CollectionEntity> all = collectionRepository.findAll();
        List<Long> collectionIds = all.stream()
                              .map(CollectionEntity::getId)
                              .collect(Collectors.toList());
        System.out.println("coll ids");
        System.out.println(collectionIds);
        System.out.println("writing photo to db: " + photoFileName);
        Optional<CollectionEntity> someCollectionThatMightExist = collectionRepository.findById(intendedCollectionId);
        if (someCollectionThatMightExist.isPresent()) {
            // write the photo using that collection
            PhotoEntity newPhoto = new PhotoEntity(photoFileName, creationTime);
            newPhoto.setCollection(someCollectionThatMightExist.get());
            System.out.println("Saving text from " + photoFileName);
            // fixme: has to have a collection set
            PhotoEntity savedPhoto = photoRepository.save(newPhoto); // store the photo's path and get its id for the next write.
            return savedPhoto;
        }
        // if the collection isn't there yet, create it because
        // you can't create a photo that has no collection
        CollectionEntity fromScratch = new CollectionEntity();
        String defaultCollectionlabel = String.valueOf(creationTime);
        fromScratch.setId(intendedCollectionId);
        fromScratch.setLabel(defaultCollectionlabel);
        CollectionEntity fromScratchButSaved = collectionRepository.save(fromScratch);

        PhotoEntity newPhoto = new PhotoEntity(photoFileName, creationTime);
        newPhoto.setCollection(fromScratchButSaved);
        System.out.println(fromScratchButSaved);
        System.out.println("[237rm] Saving text from " + photoFileName);
        System.out.println(newPhoto.toString());
        // fixme: has to have a collection set
        PhotoEntity savedPhoto = photoRepository.save(newPhoto); // store the photo's path and get its id for the next write.
        System.out.println("does this print? 202rm");
        return savedPhoto;
    }   

    public List<PhotoEntity> findAllPhotos() {
        return photoRepository.findAll();
    }
}
