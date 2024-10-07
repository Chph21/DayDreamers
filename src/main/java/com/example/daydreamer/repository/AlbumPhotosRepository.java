package com.example.daydreamer.repository;

import com.example.daydreamer.entity.AlbumPhotos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumPhotosRepository extends JpaRepository<AlbumPhotos, String> {
}
