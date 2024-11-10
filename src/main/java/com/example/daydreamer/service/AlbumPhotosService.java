package com.example.daydreamer.service;

import com.example.daydreamer.entity.Album;
import com.example.daydreamer.entity.AlbumPhotos;
import com.example.daydreamer.model.albumPhotos.AlbumPhotosRequest;
import com.example.daydreamer.model.albumPhotos.AlbumPhotosResponse;
import com.example.daydreamer.repository.AlbumPhotosRepository;
import com.example.daydreamer.repository.AlbumRepository;
import com.example.daydreamer.specification.GenericSpecification;
import com.example.daydreamer.utils.CloudinaryUtils;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumPhotosService {
    private final Logger LOGGER = LoggerFactory.getLogger(AlbumPhotosService.class);
    private final AlbumPhotosRepository albumPhotosRepository;
    private final AlbumRepository albumRepository;
    private final CloudinaryUtils cloudinaryUtils;

    public List<AlbumPhotosResponse> searchAlbumPhotos(String albumId,
                                                        String pictureLink,
                                                        int page,
                                                        int limit) {
        LOGGER.info("Searching albumPhotos with dynamic criteria");

        Specification<AlbumPhotos> spec = buildSpecification(albumId, pictureLink);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<AlbumPhotos> albumPhotos = albumPhotosRepository.findAll(spec, pageable);

        return albumPhotos.stream()
                .map(this::albumPhotosResponseGenerator)
                .collect(Collectors.toList());
    }

    private Specification<AlbumPhotos> buildSpecification(String albumId,
                                                          String pictureLink
                                                      ) {
        Specification<AlbumPhotos> spec = Specification.where(null);

        spec = GenericSpecification.addSpecification(spec, albumId, "album.id", "equal");
        spec = GenericSpecification.addSpecification(spec, pictureLink, "pictureLink", "equal");

        return spec;
    }

    public List<AlbumPhotosResponse> findAll(int page, int limit) {
        LOGGER.info("Find all albumPhotos");
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<AlbumPhotos> albumPhotos = albumPhotosRepository.findAll(pageable);
        if (albumPhotos.isEmpty()) {
            LOGGER.warn("No albumPhotos were found!");
        }

        return albumPhotos.stream()
                .map(this::albumPhotosResponseGenerator)
                .collect(Collectors.toList());
    }

    public AlbumPhotosResponse findById(String id) {
        LOGGER.info("Find albumPhotos with id " + id);
        Optional<AlbumPhotos> albumPhotos = albumPhotosRepository.findById(id);
        if (albumPhotos.isEmpty()) {
            LOGGER.warn("No albumPhotos was found!");
            return null;
        }
        return albumPhotos.map(this::albumPhotosResponseGenerator).get();
    }

//    public AlbumPhotosResponse save(AlbumPhotosRequest albumPhotosRequest) {
//        AlbumPhotos albumPhotos;
//        Optional<Album> album = albumRepository.findById(albumPhotosRequest.getAlbumId());
//        if (album.isEmpty()) {
//            throw new CustomValidationException(List.of("No album was found!"));
//        }
//
//        if (albumPhotosRequest.getId() != null) {
//            LOGGER.info("Update albumPhotos with id " + albumPhotosRequest.getId());
//            checkExist(albumPhotosRequest.getId());
//            albumPhotos = albumPhotosRepository.findById(albumPhotosRequest.getId()).get();
//        } else {
//            LOGGER.info("Create new albumPhotos");
//            albumPhotos = new AlbumPhotos();
//        }
//
//        albumPhotos.setAlbum(album.get());
//        albumPhotos.setPictureLink(albumPhotosRequest.getPictureLink());
//
//        albumPhotosRepository.save(albumPhotos);
//
//        return albumPhotosResponseGenerator(albumPhotos);
//    }

    public void delete(String id) {
        if (id != null) {
            LOGGER.info("Delete albumPhotos with id " + id);
            checkExist(id);
            AlbumPhotos albumPhotos = albumPhotosRepository.findById(id).get();
            albumPhotosRepository.delete(albumPhotos);
        }
    }

    private AlbumPhotosResponse albumPhotosResponseGenerator(AlbumPhotos albumPhotos) {
        return ResponseUtil.generateResponse(albumPhotos, AlbumPhotosResponse.class);
    }

    public AlbumPhotosResponse addImage(String albumId, MultipartFile image) throws IOException {
        AlbumPhotos albumPhotos = new AlbumPhotos();
        Optional<Album> album = albumRepository.findById(albumId);
        if (album.isEmpty()) {
            throw new CustomValidationException(List.of("No album was found!"));
        }

        albumPhotos.setAlbum(album.get());
        albumPhotos.setPictureLink(cloudinaryUtils.uploadImage(image));
        albumPhotosRepository.save(albumPhotos);

        return albumPhotosResponseGenerator(albumPhotos);
    }

    public AlbumPhotosResponse updateImage(String albumPhotoId, MultipartFile image) throws IOException {
        AlbumPhotos albumPhotos = albumPhotosRepository.findById(albumPhotoId).orElseThrow(() -> {
            LOGGER.error("No albumPhotos was found!");
            throw new CustomValidationException(List.of("No albumPhotos was found!"));
        });

        albumPhotos.setPictureLink(cloudinaryUtils.uploadImage(image));
        albumPhotosRepository.save(albumPhotos);

        return albumPhotosResponseGenerator(albumPhotos);
    }

    private void checkExist(String id) {
        if (albumPhotosRepository.findById(id).isEmpty()) {
            LOGGER.error("No albumPhotos was found!");
            throw new CustomValidationException(List.of("No albumPhotos was found!"));
        }
    }
}
