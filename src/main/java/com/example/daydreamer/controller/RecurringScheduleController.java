package com.example.daydreamer.controller;

import com.example.daydreamer.model._ResponseModel.MetaDataDTO;
import com.example.daydreamer.model.recurringSchedule.RecurringScheduleRequest;
import com.example.daydreamer.model.recurringSchedule.RecurringScheduleResponse;
import com.example.daydreamer.service.RecurringScheduleService;
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
@RequestMapping("/recurring-schedules")
@AllArgsConstructor
@Validated
public class RecurringScheduleController {
    private final RecurringScheduleService recurringScheduleService;

    @GetMapping("/search")
    public ResponseEntity<?> searchRecurringSchedules(
            @RequestParam(required = false) String studioId,
            @RequestParam(required = false) String dayOfWeek,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        List<RecurringScheduleResponse> result = recurringScheduleService.searchRecurringSchedules(studioId, dayOfWeek, status, page, limit);
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
        List<RecurringScheduleResponse> result = recurringScheduleService.findAll(page, limit);
        return ResponseUtil.getCollection(
                result,
                HttpStatus.OK,
                "Objects results fetched successfully",
                new MetaDataDTO(page < result.size(), page > 1, limit, result.size(), page)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        RecurringScheduleResponse result = recurringScheduleService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody RecurringScheduleRequest request) {
        RecurringScheduleResponse result = recurringScheduleService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody RecurringScheduleRequest request) {
        RecurringScheduleResponse result = recurringScheduleService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "Object created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        recurringScheduleService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}