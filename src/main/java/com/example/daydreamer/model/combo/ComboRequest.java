package com.example.daydreamer.model.combo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComboRequest {

    private String id;

    @NotNull(message = "Studio ID cannot be null")
    @NotBlank(message = "Studio ID cannot be blank")
    private String studioId;

    @NotNull(message = "Edited Photos cannot be null")
    private Integer editedPhotos;

    @NotNull(message = "Downloadable Photos cannot be null")
    private Integer downloadablePhotos;

    @NotNull(message = "Duration cannot be null")
    private Integer duration;

    @NotNull(message = "Price cannot be null")
    private Double price;

    @NotNull(message = "Status cannot be null")
    @NotBlank(message = "Status cannot be blank")
    private String status;

}
