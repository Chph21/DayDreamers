package com.example.daydreamer.model.albumPhotos;

import com.example.daydreamer.entity.Album;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumPhotosRequest {

    private String id;

    @NotNull(message = "Album ID cannot be null")
    @NotBlank(message = "Album ID cannot be blank")
    private String albumId;

    @NotNull(message = "Picture Link cannot be null")
    @NotBlank(message = "Picture Link cannot be blank")
    private String pictureLink;

    @NotNull(message = "Status cannot be null")
    @NotBlank(message = "Status cannot be blank")
    private String status;
}
