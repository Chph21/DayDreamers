package com.example.daydreamer.service;

import com.example.daydreamer.entity.Studio;
import com.example.daydreamer.model.studio.StudioRequest;
import com.example.daydreamer.model.studio.StudioResponse;
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
public class StudioService {
    private final Logger LOGGER = LoggerFactory.getLogger(StudioService.class);
    private final StudioRepository studioRepository;

    public List<StudioResponse> searchStudios(String name, String availableCity, String status, int page, int limit) {
        LOGGER.info("Searching studios with dynamic criteria");

        Specification<Studio> spec = buildSpecification(name, availableCity, status);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Studio> studioPage = studioRepository.findAll(spec, pageable);

        return studioPage.stream()
                .map(this::studioResponseGenerator)
                .collect(Collectors.toList());
    }

    private Specification<Studio> buildSpecification(String name, String availableCity, String status) {
        Specification<Studio> spec = Specification.where(null);

        spec = GenericSpecification.addSpecification(spec, name, "name", "like");
        spec = GenericSpecification.addSpecification(spec, availableCity, "availableCity", "equal");
        spec = GenericSpecification.addSpecification(spec, status, "status", "equal");

        return spec;
    }

    public List<StudioResponse> findAll(int page, int limit) {
        LOGGER.info("Find all studios");
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Studio> studioPage = studioRepository.findAll(pageable);
        if (studioPage.isEmpty()) {
            LOGGER.warn("No studios were found!");
        }

        return studioPage.stream()
                .map(this::studioResponseGenerator)
                .collect(Collectors.toList());
    }

    public StudioResponse findById(String id) {
        LOGGER.info("Find studio with id " + id);
        Optional<Studio> studio = studioRepository.findById(id);
        if (studio.isEmpty()) {
            LOGGER.warn("No studio was found!");
            return null;
        }
        return studio.map(this::studioResponseGenerator).get();
    }

    public StudioResponse save(StudioRequest studioRequest) {
        Studio studio;

        if (studioRequest.getId() != null) {
            LOGGER.info("Update studio with id " + studioRequest.getId());
            checkExist(studioRequest.getId());
            studio = studioRepository.findById(studioRequest.getId()).get();
        } else {
            LOGGER.info("Create new studio");
            studio = new Studio();
        }

        studio.setName(studioRequest.getName());
        studio.setLogoLink(studioRequest.getLogoLink());
        studio.setOverview(studioRequest.getOverview());
        studio.setCamera(studioRequest.getCamera());
        studio.setLanguage(studioRequest.getLanguage());
        studio.setAvailableCity(studioRequest.getAvailableCity());
        studio.setStatus(studioRequest.getStatus());

        studioRepository.save(studio);

        return studioResponseGenerator(studio);
    }

    public void delete(String id) {
        if (id != null) {
            LOGGER.info("Delete studio with id " + id);
            checkExist(id);
            Studio studio = studioRepository.findById(id).get();
            studioRepository.delete(studio);
        }
    }

    public StudioResponse studioResponseGenerator(Studio studio) {
        return ResponseUtil.generateResponse(studio, StudioResponse.class);
    }

    private void checkExist(String id) {
        if (studioRepository.findById(id).isEmpty()) {
            LOGGER.error("No studio was found!");
            throw new CustomValidationException(List.of("No studio was found!"));
        }
    }
}