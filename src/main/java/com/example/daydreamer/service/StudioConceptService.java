package com.example.daydreamer.service;

import com.example.daydreamer.entity.StudioConcept;
import com.example.daydreamer.entity.Studio;
import com.example.daydreamer.entity.Concept;
import com.example.daydreamer.model.studioConcept.StudioConceptRequest;
import com.example.daydreamer.model.studioConcept.StudioConceptResponse;
import com.example.daydreamer.repository.StudioConceptRepository;
import com.example.daydreamer.repository.StudioRepository;
import com.example.daydreamer.repository.ConceptRepository;
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
public class StudioConceptService {
    private final Logger LOGGER = LoggerFactory.getLogger(StudioConceptService.class);
    private final StudioConceptRepository studioConceptRepository;
    private final StudioRepository studioRepository;
    private final ConceptRepository conceptRepository;

    public List<StudioConceptResponse> searchStudioConcepts(String studioId, String conceptId, String status, int page, int limit) {
        LOGGER.info("Searching studio concepts with dynamic criteria");

        Specification<StudioConcept> spec = buildSpecification(studioId, conceptId, status);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<StudioConcept> studioConceptPage = studioConceptRepository.findAll(spec, pageable);

        return studioConceptPage.stream()
                .map(this::studioConceptResponseGenerator)
                .collect(Collectors.toList());
    }

    private Specification<StudioConcept> buildSpecification(String studioId, String conceptId, String status) {
        Specification<StudioConcept> spec = Specification.where(null);

        spec = GenericSpecification.addSpecification(spec, studioId, "studio.id", "equal");
        spec = GenericSpecification.addSpecification(spec, conceptId, "concept.id", "equal");
        spec = GenericSpecification.addSpecification(spec, status, "status", "equal");

        return spec;
    }

    public List<StudioConceptResponse> findAll(int page, int limit) {
        LOGGER.info("Find all studio concepts");
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<StudioConcept> studioConceptPage = studioConceptRepository.findAll(pageable);
        if (studioConceptPage.isEmpty()) {
            LOGGER.warn("No studio concepts were found!");
        }

        return studioConceptPage.stream()
                .map(this::studioConceptResponseGenerator)
                .collect(Collectors.toList());
    }

    public StudioConceptResponse findById(String id) {
        LOGGER.info("Find studio concept with id " + id);
        Optional<StudioConcept> studioConcept = studioConceptRepository.findById(id);
        if (studioConcept.isEmpty()) {
            LOGGER.warn("No studio concept was found!");
            return null;
        }
        return studioConcept.map(this::studioConceptResponseGenerator).get();
    }

    public StudioConceptResponse save(StudioConceptRequest studioConceptRequest) {
        StudioConcept studioConcept;
        Optional<Studio> studio = studioRepository.findById(studioConceptRequest.getStudioId());
        Optional<Concept> concept = conceptRepository.findById(studioConceptRequest.getConceptId());
        if (studio.isEmpty() || concept.isEmpty()) {
            throw new CustomValidationException(List.of("No studio or concept was found!"));
        }

        if (studioConceptRequest.getId() != null) {
            LOGGER.info("Update studio concept with id " + studioConceptRequest.getId());
            checkExist(studioConceptRequest.getId());
            studioConcept = studioConceptRepository.findById(studioConceptRequest.getId()).get();
        } else {
            LOGGER.info("Create new studio concept");
            studioConcept = new StudioConcept();
        }

        studioConcept.setStudio(studio.get());
        studioConcept.setPrice(studioConceptRequest.getPrice());
        studioConcept.setConcept(concept.get());
        studioConcept.setStatus(studioConceptRequest.getStatus());
        LOGGER.info("StudioId: " + studioConcept.getStudio().getId());
        LOGGER.info("ConceptId: " + studioConcept.getConcept().getId());
        studioConceptRepository.save(studioConcept);

        return studioConceptResponseGenerator(studioConcept);
    }

    public void delete(String id) {
        if (id != null) {
            LOGGER.info("Delete studio concept with id " + id);
            checkExist(id);
            StudioConcept studioConcept = studioConceptRepository.findById(id).get();
            studioConceptRepository.delete(studioConcept);
        }
    }

    public StudioConceptResponse studioConceptResponseGenerator(StudioConcept studioConcept) {
        return ResponseUtil.generateResponse(studioConcept, StudioConceptResponse.class);
    }

    private void checkExist(String id) {
        if (studioConceptRepository.findById(id).isEmpty()) {
            LOGGER.error("No studio concept was found!");
            throw new CustomValidationException(List.of("No studio concept was found!"));
        }
    }
}