package com.example.daydreamer.model.studio;

import com.example.daydreamer.enums.ShootingTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudioRequest {

    private String id;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Logo Link cannot be null")
    @NotBlank(message = "Logo Link cannot be blank")
    private String logoLink;

    @NotNull(message = "Overview cannot be null")
    @NotBlank(message = "Overview cannot be blank")
    private String overview;

    @NotNull(message = "Camera cannot be null")
    @NotBlank(message = "Camera cannot be blank")
    private String camera;

    @NotNull(message = "Language cannot be null")
    @NotBlank(message = "Language cannot be blank")
    private String language;

    @NotNull(message = "Shooting Types cannot be null")
    private List<ShootingTypeEnum> shootingTypes;

    @NotNull(message = "Available City cannot be null")
    @NotBlank(message = "Available City cannot be blank")
    private String availableCity;

    @NotNull(message = "Status cannot be null")
    @NotBlank(message = "Status cannot be blank")
    private String status;
}