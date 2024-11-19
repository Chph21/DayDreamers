package com.example.daydreamer.model.updateShootingType;

import com.example.daydreamer.enums.ShootingTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShootingTypeRequest {

    private String id;

    @NotNull(message = "StudioId cannot be null")
    @NotBlank(message = "StudioId cannot be blank")
    private String studioId;

    @NotNull(message = "shootingType cannot be null")
    @NotBlank(message = "ShootingType cannot be blank")
    private ShootingTypeEnum shootingType;

    @NotNull(message = "Price cannot be null")
    private Double price;

}
