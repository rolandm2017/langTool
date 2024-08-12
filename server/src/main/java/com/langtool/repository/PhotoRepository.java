package com.langtool.repository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.persistence.*;
import java.util.List;

import com.langtool.model.PhotoEntity;

/*
 * 
 * This could have just as easily been called the CollectionRepository.
 * Perhaps one is still needed.
 * 
 */

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {
    // The basic CRUD operations are automatically provided by JpaRepository
}
