package com.example.daydreamer.controller;

import com.example.daydreamer.model._ResponseModel.MetaDataDTO;
import com.example.daydreamer.model.closedDay.ClosedDayRequest;
import com.example.daydreamer.model.closedDay.ClosedDayResponse;
import com.example.daydreamer.service.ClosedDayService;
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
@RequestMapping("/closed-days")
@RequiredArgsConstructor
@Validated
public class ClosedDayController {
    private final ClosedDayService closedDayService;

    @GetMapping("/search")
    public ResponseEntity<?> searchClosedDays(
            @RequestParam(required = false) String studioId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        List<ClosedDayResponse> result = closedDayService.searchClosedDays(studioId, status, page, limit);
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
        List<ClosedDayResponse> result = closedDayService.findAll(page, limit);
        return ResponseUtil.getCollection(
                result,
                HttpStatus.OK,
                "Objects results fetched successfully",
                new MetaDataDTO(page < result.size(), page > 1, limit, result.size(), page)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        ClosedDayResponse result = closedDayService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody ClosedDayRequest request) {
        ClosedDayResponse result = closedDayService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ClosedDayRequest request) {
        ClosedDayResponse result = closedDayService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "Object created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        closedDayService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}