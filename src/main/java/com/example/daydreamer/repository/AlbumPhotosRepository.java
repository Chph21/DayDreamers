package com.example.daydreamer.repository;

import com.example.daydreamer.entity.AlbumPhotos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AlbumPhotosRepository extends JpaRepository<AlbumPhotos, String>, JpaSpecificationExecutor<AlbumPhotos> {
}
