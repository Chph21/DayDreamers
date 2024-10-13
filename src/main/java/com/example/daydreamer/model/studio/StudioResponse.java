package com.example.daydreamer.model.studio;

import com.example.daydreamer.model._ResponseModel.GenericResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudioResponse extends GenericResponse {
    private String id;

    private String name;

    private String logoLink;

    private String overview;

    private String camera;

    private String language;

    private String availableCity;

    private String status;
}