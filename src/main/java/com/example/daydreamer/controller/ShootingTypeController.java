package com.example.daydreamer.controller;

import com.example.daydreamer.model.updateShootingType.UpdateShootingTypeRequest;
import com.example.daydreamer.model.updateShootingType.UpdateShootingTypeResponse;
import com.example.daydreamer.service.ShootingTypeService;
import com.example.daydreamer.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shootingTypes")
@RequiredArgsConstructor
@Validated
public class ShootingTypeController {
    private final ShootingTypeService shootingTypeService;
    @GetMapping("/{studioId}")
    public ResponseEntity<?> getByStudioId(@PathVariable(name = "studioId") String id) {
        List<UpdateShootingTypeResponse> result = shootingTypeService.findShootingTypesOfStudio(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PostMapping
    public ResponseEntity<?> createShootingType(@RequestBody UpdateShootingTypeRequest request) {
        UpdateShootingTypeResponse result = shootingTypeService.updateShootingTypeOfStudio(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Shooting Type created successfully");
    }
    @PutMapping
    public ResponseEntity<?> updateShootingType(@RequestBody UpdateShootingTypeRequest request) {
        UpdateShootingTypeResponse result = shootingTypeService.updateShootingTypeOfStudio(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Shooting Type updated successfully");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteShootingType(@RequestParam String id) {
        shootingTypeService.deleteShootingTypeOfStudio(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Shooting Type deleted successfully");
    }
}
