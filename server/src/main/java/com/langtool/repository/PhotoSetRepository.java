package com.langtool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.persistence.*;
import java.util.List;

import com.langtool.model.Photo;

@Repository
public interface PhotoSetRepository extends JpaRepository<Photo, Long> {
    // The basic CRUD operations are automatically provided by JpaRepository
}
