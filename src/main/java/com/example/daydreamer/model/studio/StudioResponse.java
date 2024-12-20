package com.example.daydreamer.model.studio;

import com.example.daydreamer.model._ResponseModel.GenericResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudioResponse extends GenericResponse {
    private String id;

    private String name;

    private Long amount;

    private String bankAccount;

    private String bankName;

    private List<String> shootingTypesIds;

    private List<String> albumsIds;

    private String logoLink;

    private String overview;

    private String camera;

    private String availableCity;

    private String status;
}