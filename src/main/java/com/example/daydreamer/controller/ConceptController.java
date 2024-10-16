package com.example.daydreamer.controller;

import com.example.daydreamer.model._ResponseModel.MetaDataDTO;
import com.example.daydreamer.model.concept.ConceptRequest;
import com.example.daydreamer.model.concept.ConceptResponse;
import com.example.daydreamer.service.ConceptService;
import com.example.daydreamer.utils.ResponseUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/concepts")
@AllArgsConstructor
@Validated
public class ConceptController {
    private final ConceptService conceptService;

    @GetMapping("/search")
    public ResponseEntity<?> searchConcepts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        List<ConceptResponse> result = conceptService.searchConcepts(name, status, page, limit);
        return ResponseUtil.getCollection(
                result,
                HttpStatus.OK,
                "Search results fetched successfully",
                new MetaDataDTO(page < result.size(), page > 1, limit, result.size(), page)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<ConceptResponse> result = conceptService.findAll(page, limit);
        return ResponseUtil.getCollection(
                result,
                HttpStatus.OK,
                "Objects results fetched successfully",
                new MetaDataDTO(page < result.size(), page > 1, limit, result.size(), page)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        ConceptResponse result = conceptService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody ConceptRequest request) {
        ConceptResponse result = conceptService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ConceptRequest request) {
        ConceptResponse result = conceptService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "Object created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        conceptService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}