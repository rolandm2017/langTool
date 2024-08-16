package com.langtool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import com.langtool.object.TextGroup;
import com.langtool.repository.PhotoRepository;
import com.langtool.repository.TextGroupRepository;

import jakarta.persistence.EntityNotFoundException;

import com.langtool.repository.CollectionRepository;
import com.langtool.model.CollectionEntity;
import com.langtool.model.PhotoEntity;
import com.langtool.model.TextGroupEntity;
import com.langtool.model.WordEntity;
import com.langtool.dao.CollectionDao;
import com.langtool.dto.CollectionDto;
import com.langtool.dto.EntityToDtoConverter;
import com.langtool.dto.PhotoDto;

import java.util.HashSet;;

@Service
public class CollectionService {

    // @Autowired
    // private PhotoRepository photoRepository;
    
    // @Autowired
    // private TextGroupRepository textGroupRepository;

    @Autowired
    private CollectionDao collectionDao;

    public CollectionDto getCollectionById(Long id) {
        Optional<CollectionEntity> maybeEntity = collectionDao.findByCollectionId(id);

        if (maybeEntity.isPresent()) {
            CollectionEntity entity = maybeEntity.get();
            CollectionDto dto = EntityToDtoConverter.convertCollectionEntityToDto(entity);
            return dto;
        }
        return new CollectionDto(); // fail
    }

    public Long getHighestCollectionId() {
        Long highest = collectionDao.findHighestId();
        System.out.println("Highest collection ID: " + String.valueOf(highest));
        boolean absolutelyNoRows = highest == null;
        if (absolutelyNoRows) {
            return null;
        }
        System.out.println("45rm");
        System.out.println(highest);
        return highest;
    }

    public List<CollectionDto> getCollections() {
        List<CollectionEntity> allCollections = collectionDao.findAllCollections();
        System.out.println("Number of collections: " + allCollections.size());
        List<CollectionDto> forTransfer = EntityToDtoConverter.convertCollectionEntitiesToDtos(allCollections);
        return forTransfer;
       
    }

    public boolean updateCollectionLabel(Long updatedCollectionId, String newLabel) {
        return collectionDao.updateCollectionLabel(updatedCollectionId, newLabel);
    }

}