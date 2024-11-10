package com.example.daydreamer.model.studioConcept;

import com.example.daydreamer.model._ResponseModel.GenericResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudioConceptResponse extends GenericResponse {
    private String id;

    private String studioId;

    private String conceptId;

    private double price;

    private String status;
}