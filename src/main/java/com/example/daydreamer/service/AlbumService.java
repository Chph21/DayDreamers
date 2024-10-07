package com.example.daydreamer.service;

import com.example.daydreamer.entity.AlbumPhotos;
import com.example.daydreamer.entity.Booking;
import com.example.daydreamer.entity.Album;
import com.example.daydreamer.entity.Studio;
import com.example.daydreamer.model.album.AlbumRequest;
import com.example.daydreamer.model.album.AlbumResponse;
import com.example.daydreamer.repository.AlbumPhotosRepository;
import com.example.daydreamer.repository.AlbumRepository;
import com.example.daydreamer.repository.StudioRepository;
import com.example.daydreamer.specification.GenericSpecification;
import com.example.daydreamer.utils.CustomValidationException;
import com.example.daydreamer.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final Logger LOGGER = LoggerFactory.getLogger(AlbumService.class);
    private final AlbumRepository albumRepository;
    private final StudioRepository studioRepository;
    private final AlbumPhotosRepository albumPhotosRepository;

    public List<AlbumResponse> searchAlbums(String studioId,
                                            String name,
                                            Double price,
                                            String status,
                                            int page,
                                            int limit) {
        LOGGER.info("Searching albums with dynamic criteria");

        Specification<Album> spec = buildSpecification(studioId, name, price, status);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Album> albums = albumRepository.findAll(spec, pageable);

        return albums.stream()
                .map(this::albumResponseGenerator)
                .collect(Collectors.toList());
    }

    private Specification<Album> buildSpecification(String studioId,
                                                    String name,
                                                    Double price,
                                                    String status) {
        Specification<Album> spec = Specification.where(null);

        spec = GenericSpecification.addSpecification(spec, studioId, "studio.id", "equal");
        spec = GenericSpecification.addSpecification(spec, name, "name", "like");
        spec = GenericSpecification.addSpecification(spec, price, "price", "equal");
        spec = GenericSpecification.addSpecification(spec, status, "status", "equal");

        return spec;
    }

    public List<AlbumResponse> findAll(int page, int limit) {
        LOGGER.info("Find all albums");
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Album> albums = albumRepository.findAll(pageable);
        if (albums.isEmpty()) {
            LOGGER.warn("No albums were found!");
        }

        return albums.stream()
                .map(this::albumResponseGenerator)
                .collect(Collectors.toList());
    }

    public AlbumResponse findById(String id) {
        LOGGER.info("Find album with id " + id);
        Optional<Album> album = albumRepository.findById(id);
        if (album.isEmpty()) {
            LOGGER.warn("No album was found!");
            return null;
        }
        return album.map(this::albumResponseGenerator).get();
    }

    public AlbumResponse save(AlbumRequest albumRequest) {
        Album album;
        List<AlbumPhotos> albumPhotos = albumPhotosRepository.findAllById(albumRequest.getAlbumPhotoIds());
        Optional<Studio> studio = studioRepository.findById(albumRequest.getStudioId());
        if ((!albumRequest.getAlbumPhotoIds().isEmpty() && albumPhotos.isEmpty()) || studio.isEmpty()) {
            throw new CustomValidationException(List.of("No bookings or studio was found!"));
        }

        if (albumRequest.getId() != null) {
            LOGGER.info("Update album with id " + albumRequest.getId());
            checkExist(albumRequest.getId());
            album = albumRepository.findById(albumRequest.getId()).get();
        } else {
            LOGGER.info("Create new album");
            album = new Album();
        }

        album.setAlbumPhotos(albumPhotos);
        album.setStudio(studio.get());
        album.setPrice(albumRequest.getPrice());
        album.setName(albumRequest.getName());
        album.setStatus(albumRequest.getStatus());

        albumRepository.save(album);

        return albumResponseGenerator(album);
    }

    public void delete(String id) {
        if (id != null) {
            LOGGER.info("Delete album with id " + id);
            checkExist(id);
            Album album = albumRepository.findById(id).get();
            albumRepository.delete(album);
        }
    }

    private AlbumResponse albumResponseGenerator(Album album) {
        return ResponseUtil.generateResponse(album, AlbumResponse.class);
    }

    private void checkExist(String id) {
        if (albumRepository.findById(id).isEmpty()) {
            LOGGER.error("No album was found!");
            throw new CustomValidationException(List.of("No album was found!"));
        }
    }
}
