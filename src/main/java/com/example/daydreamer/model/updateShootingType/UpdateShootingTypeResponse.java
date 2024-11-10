package com.example.daydreamer.model.updateShootingType;

import com.example.daydreamer.enums.ShootingTypeEnum;
import com.example.daydreamer.model._ResponseModel.GenericResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateShootingTypeResponse extends GenericResponse {
    private String id;
    private String studioId;
    private ShootingTypeEnum shootingType;
    private double price;

}
