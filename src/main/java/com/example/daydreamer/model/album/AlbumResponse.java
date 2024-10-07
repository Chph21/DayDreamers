package com.example.daydreamer.model.album;

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
public class AlbumResponse extends GenericResponse {

    private String id;

    private String studioId;

    private List<String> albumPhotoIds;

    private String name;

    private Double price;
}
