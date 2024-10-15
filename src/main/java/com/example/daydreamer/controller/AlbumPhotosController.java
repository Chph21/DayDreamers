package com.example.daydreamer.controller;

import com.example.daydreamer.model._ResponseModel.MetaDataDTO;
import com.example.daydreamer.model.albumPhotos.AlbumPhotosRequest;
import com.example.daydreamer.model.albumPhotos.AlbumPhotosResponse;
import com.example.daydreamer.service.AlbumPhotosService;
import com.example.daydreamer.utils.ResponseUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albumPhotos")
@RequiredArgsConstructor
@Validated
public class AlbumPhotosController {
    
    private final AlbumPhotosService albumPhotosService;

    @GetMapping("/search")
    public ResponseEntity<?> searchAlbumPhotos(
            @RequestParam(required = false) String albumId,
            @RequestParam(required = false) String pictureLink,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        List<AlbumPhotosResponse> result = albumPhotosService.searchAlbumPhotos(albumId, pictureLink, status, page, limit);
        return ResponseUtil.getCollection(
                result,
                HttpStatus.OK,
                "Search results fetched successfully",
                new MetaDataDTO(page < result.size(),page > 1, limit, result.size(), page)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int limit) {
        List<AlbumPhotosResponse> result = albumPhotosService.findAll(page, limit);
        return ResponseUtil.getCollection(
                result,
                HttpStatus.OK,
                "Search results fetched successfully",
                new MetaDataDTO(page < result.size(),page > 1, limit, result.size(), page)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        AlbumPhotosResponse result = albumPhotosService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody AlbumPhotosRequest request) {
        AlbumPhotosResponse result = albumPhotosService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody AlbumPhotosRequest request) {
        AlbumPhotosResponse result = albumPhotosService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "Object created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        albumPhotosService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}
