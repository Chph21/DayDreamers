package com.example.daydreamer.controller;

import com.example.daydreamer.enums.AccountRole;
import com.example.daydreamer.model._ResponseModel.MetaDataDTO;
import com.example.daydreamer.model.account.AccountRequest;
import com.example.daydreamer.model.account.AccountResponse;
import com.example.daydreamer.service.AccountService;
import com.example.daydreamer.utils.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Validated
public class AccountController {
    private final AccountService AccountService;

    @GetMapping("/search")
    public ResponseEntity<?> searchAccount(
            @RequestParam(required = false) String studioId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String dob,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String nationality,
            @RequestParam(required = false) AccountRole role,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        List<AccountResponse> result = AccountService.searchAccounts(
                studioId,
                username,
                fullName,
                address,
                dob,
                gender,
                phoneNumber,
                nationality,
                role,
                status,
                page,
                limit
                );
        return ResponseUtil.getCollection(
                result,
                HttpStatus.OK,
                "Search results fetched successfully",
                new MetaDataDTO(page < result.size(),page > 1, limit, result.size(), page)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<AccountResponse> result = AccountService.findAll(page, limit);
        return ResponseUtil.getCollection(
                result,
                HttpStatus.OK,
                "Objects results fetched successfully",
                new MetaDataDTO(page < result.size(),page > 1, limit, result.size(), page)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        AccountResponse result = AccountService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody AccountRequest request) {
        AccountResponse result = AccountService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }
    @PostMapping("uploadAvatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam String accountId, @RequestParam MultipartFile avatar) {
        AccountResponse result;
        try {
            result = AccountService.uploadAvatar(accountId, avatar);
        } catch (IOException e) {
           return ResponseUtil.error("Failed to upload avatar", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Avatar uploaded successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        AccountService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}
