package com.example.daydreamer.service;

import com.example.daydreamer.entity.Concept;
import com.example.daydreamer.model.concept.ConceptRequest;
import com.example.daydreamer.model.concept.ConceptResponse;
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
public class ConceptService {
    private final Logger LOGGER = LoggerFactory.getLogger(ConceptService.class);
    private final ConceptRepository conceptRepository;

    public List<ConceptResponse> searchConcepts(String name, String status, int page, int limit) {
        LOGGER.info("Searching concepts with dynamic criteria");

        Specification<Concept> spec = buildSpecification(name, status);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Concept> conceptPage = conceptRepository.findAll(spec, pageable);

        return conceptPage.stream()
                .map(this::conceptResponseGenerator)
                .collect(Collectors.toList());
    }

    private Specification<Concept> buildSpecification(String name, String status) {
        Specification<Concept> spec = Specification.where(null);

        spec = GenericSpecification.addSpecification(spec, name, "name", "like");
        spec = GenericSpecification.addSpecification(spec, status, "status", "equal");

        return spec;
    }

    public List<ConceptResponse> findAll(int page, int limit) {
        LOGGER.info("Find all concepts");
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Concept> conceptPage = conceptRepository.findAll(pageable);
        if (conceptPage.isEmpty()) {
            LOGGER.warn("No concepts were found!");
        }

        return conceptPage.stream()
                .map(this::conceptResponseGenerator)
                .collect(Collectors.toList());
    }

    public ConceptResponse findById(String id) {
        LOGGER.info("Find concept with id " + id);
        Optional<Concept> concept = conceptRepository.findById(id);
        if (concept.isEmpty()) {
            LOGGER.warn("No concept was found!");
            return null;
        }
        return concept.map(this::conceptResponseGenerator).get();
    }

    public ConceptResponse save(ConceptRequest conceptRequest) {
        Concept concept;

        if (conceptRequest.getId() != null) {
            LOGGER.info("Update concept with id " + conceptRequest.getId());
            checkExist(conceptRequest.getId());
            concept = conceptRepository.findById(conceptRequest.getId()).get();
        } else {
            LOGGER.info("Create new concept");
            concept = new Concept();
        }

        concept.setName(conceptRequest.getName());
        concept.setDescription(conceptRequest.getDescription());
        concept.setStatus(conceptRequest.getStatus());

        conceptRepository.save(concept);

        return conceptResponseGenerator(concept);
    }

    public void delete(String id) {
        if (id != null) {
            LOGGER.info("Delete concept with id " + id);
            checkExist(id);
            Concept concept = conceptRepository.findById(id).get();
            conceptRepository.delete(concept);
        }
    }

    public ConceptResponse conceptResponseGenerator(Concept concept) {
        return ResponseUtil.generateResponse(concept, ConceptResponse.class);
    }

    private void checkExist(String id) {
        if (conceptRepository.findById(id).isEmpty()) {
            LOGGER.error("No concept was found!");
            throw new CustomValidationException(List.of("No concept was found!"));
        }
    }
}