package com.example.daydreamer.controller;

import com.example.daydreamer.model._ResponseModel.MetaDataDTO;
import com.example.daydreamer.model.studio.StudioRequest;
import com.example.daydreamer.model.studio.StudioResponse;
import com.example.daydreamer.service.StudioService;
import com.example.daydreamer.utils.ResponseUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/studios")
@RequiredArgsConstructor
@Validated
public class StudioController {
    private final StudioService studioService;

    @GetMapping("/search")
    public ResponseEntity<?> searchStudios(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String availableCity,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        List<StudioResponse> result = studioService.searchStudios(name, availableCity, status, page, limit);
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
        List<StudioResponse> result = studioService.findAll(page, limit);
        return ResponseUtil.getCollection(
                result,
                HttpStatus.OK,
                "Objects results fetched successfully",
                new MetaDataDTO(page < result.size(), page > 1, limit, result.size(), page)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        StudioResponse result = studioService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody StudioRequest request) {
        StudioResponse result = studioService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdrawMoney(@RequestParam @NotNull @NotBlank String id,
                                           @RequestParam @NotNull Long amount) {
        StudioResponse result = studioService.withdrawMoney(id, amount);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody StudioRequest request) {
        StudioResponse result = studioService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "Object created successfully");
    }

    @PostMapping("/uploadLogo")
    public ResponseEntity<?> uploadLogo(@RequestParam("logo") MultipartFile logo, @RequestParam String studioId) throws IOException {
        StudioResponse result;
        try {
            result = studioService.uploadLogo(studioId, logo);
        } catch (IOException e) {
            return ResponseUtil.error("Failed to upload logo",e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "Logo uploaded successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        studioService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}