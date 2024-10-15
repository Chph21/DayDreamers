package com.example.daydreamer.controller;

import com.example.daydreamer.model._ResponseModel.MetaDataDTO;
import com.example.daydreamer.model.combo.ComboRequest;
import com.example.daydreamer.model.combo.ComboResponse;
import com.example.daydreamer.service.ComboService;
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
@RequestMapping("/combos")
@RequiredArgsConstructor
@Validated
public class ComboController {

    private final ComboService comboService;

    @GetMapping("/search")
    public ResponseEntity<?> searchCombo(
            @RequestParam(required = false) String studioId,
            @RequestParam(required = false) Integer editedPhotos,
            @RequestParam(required = false) Integer downloadablePhotos,
            @RequestParam(required = false) Integer duration,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        List<ComboResponse> result = comboService.searchCombos(studioId, editedPhotos, downloadablePhotos, duration, price, status, page, limit);
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
        List<ComboResponse> result = comboService.findAll(page, limit);
        return ResponseUtil.getCollection(
                result,
                HttpStatus.OK,
                "Search results fetched successfully",
                new MetaDataDTO(page < result.size(),page > 1, limit, result.size(), page)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        ComboResponse result = comboService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody ComboRequest request) {
        ComboResponse result = comboService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ComboRequest request) {
        ComboResponse result = comboService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "Object created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        comboService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}
