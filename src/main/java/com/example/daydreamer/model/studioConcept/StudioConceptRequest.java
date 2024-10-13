package com.example.daydreamer.model.studioConcept;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudioConceptRequest {

    private String id;

    @NotNull(message = "Studio ID cannot be null")
    @NotBlank(message = "Studio ID cannot be blank")
    private String studioId;

    @NotNull(message = "Concept ID cannot be null")
    @NotBlank(message = "Concept ID cannot be blank")
    private String conceptId;

    @NotNull(message = "Status cannot be null")
    @NotBlank(message = "Status cannot be blank")
    private String status;
}