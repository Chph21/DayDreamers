package com.example.daydreamer.controller;

import com.example.daydreamer.model._ResponseModel.ResponseDTO;
import com.example.daydreamer.model.combo.ComboRequest;
import com.example.daydreamer.model.combo.ComboResponse;
import com.example.daydreamer.service.ComboService;
import com.example.daydreamer.utils.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class ComboController {

    private ComboService comboService;

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO> searchCombo(
            @RequestParam(required = false) String studioId,
            @RequestParam(required = false) Integer editedPhotos,
            @RequestParam(required = false) Integer downloadablePhotos,
            @RequestParam(required = false) Integer duration,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String status) {
        List<ComboResponse> result = comboService.searchCombos(studioId, editedPhotos, downloadablePhotos, duration, price, status);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Search results fetched successfully");
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getAll() {
        List<ComboResponse> result = comboService.findAll();
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable String id) {
        ComboResponse result = comboService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> update(@Valid @RequestBody ComboRequest request) {
        ComboResponse result = comboService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@Valid @RequestBody ComboRequest request) {
        ComboResponse result = comboService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "Object created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable String id) {
        comboService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}
