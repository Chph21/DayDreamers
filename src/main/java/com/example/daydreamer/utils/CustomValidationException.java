package com.example.daydreamer.utils;

import lombok.Getter;

import java.util.List;

@Getter
public class CustomValidationException extends RuntimeException {
    private final List<String> errors;

    public CustomValidationException(List<String> errors) {
        super(String.join(", ", errors));
        this.errors = errors;
    }

}