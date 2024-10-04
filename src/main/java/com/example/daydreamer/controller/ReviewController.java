package com.example.daydreamer.controller;

import com.example.daydreamer.model.ResponseModel.ResponseDTO;
import com.example.daydreamer.model.review.ReviewRequest;
import com.example.daydreamer.model.review.ReviewResponse;
import com.example.daydreamer.service.ReviewService;
import com.example.daydreamer.utils.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Validated
public class ReviewController {
    private ReviewService reviewService;

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO> searchReviews(
            @RequestParam(required = false) String bookingId,
            @RequestParam(required = false) String studioId,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) String status) {
        List<ReviewResponse> result = reviewService.searchReviews(bookingId, studioId, rating, status);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Search results fetched successfully");
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getAll() {
        List<ReviewResponse> result = reviewService.findAll();
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable String id) {
        ReviewResponse result = reviewService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> update(@Valid @RequestBody ReviewRequest request) {
        ReviewResponse result = reviewService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@Valid @RequestBody ReviewRequest request) {
        ReviewResponse result = reviewService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "Object created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable String id) {
        reviewService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}
