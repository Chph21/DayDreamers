package com.example.daydreamer.model.albumPhotos;

import com.example.daydreamer.model._ResponseModel.GenericResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumPhotosResponse extends GenericResponse {

    private String id;

    private String albumId;

    private String pictureLink;

    private String status;
}
