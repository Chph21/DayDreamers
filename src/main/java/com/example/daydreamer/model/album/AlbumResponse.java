package com.example.daydreamer.model.album;

import com.example.daydreamer.model._ResponseModel.GenericResponse;

import java.util.List;

public class AlbumResponse extends GenericResponse {

    private String id;

    private String studioId;

    private List<String> albumPhotoIds;

    private String name;

    private Double price;
}
