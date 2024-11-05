package com.example.daydreamer.service;

import com.example.daydreamer.entity.ShootingType;
import com.example.daydreamer.entity.Studio;
import com.example.daydreamer.model.updateShootingType.UpdateShootingTypeRequest;
import com.example.daydreamer.model.updateShootingType.UpdateShootingTypeResponse;
import com.example.daydreamer.repository.ShootingTypeRepository;
import com.example.daydreamer.repository.StudioRepository;
import com.example.daydreamer.utils.CustomValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShootingTypeService {
    private final Logger LOGGER = LoggerFactory.getLogger(ShootingTypeService.class);
    private final ShootingTypeRepository shootingTypeRepository;
    private final StudioRepository studioRepository;
    public List<UpdateShootingTypeResponse> findShootingTypesOfStudio(String studioId) {
        LOGGER.info("Find all shooting types of studio with id: {}", studioId);
        //check Studio is exist or not?
        Studio studio = studioRepository.findById(studioId).orElseThrow(() -> new CustomValidationException(Collections.singletonList("Studio not found")));
        List<ShootingType> list = shootingTypeRepository.findAllByStudio(studio);
        List<UpdateShootingTypeResponse> response = new ArrayList<>();
        list.forEach(shootingType -> response.add(new UpdateShootingTypeResponse(shootingType.getId(), shootingType.getStudio().getId(),
                shootingType.getType(), shootingType.getPrice())));
        return response;
    }

    public UpdateShootingTypeResponse updateShootingTypeOfStudio(UpdateShootingTypeRequest request) {
        LOGGER.info("Update shooting type of studio with id: {}", request.getId());
        //check shooting type is exist or not?
        ShootingType shootingType = shootingTypeRepository.findShootingTypeById(request.getId());
        if(shootingType == null) {
            LOGGER.info("Shooting type not found, creating new shooting type");
            ShootingType type = new ShootingType();
            type.setStudio(studioRepository.findById(request.getStudioId()).orElseThrow(() -> new CustomValidationException(Collections.singletonList("Studio not found"))));
            type.setPrice(request.getPrice());
            type.setType(request.getShootingType());
        }
        shootingType.setPrice(request.getPrice());
        shootingType.setType(request.getShootingType());
        shootingTypeRepository.save(shootingType);

        UpdateShootingTypeResponse response = new UpdateShootingTypeResponse(shootingType.getId(), shootingType.getStudio().getId(),
                shootingType.getType(), shootingType.getPrice());
    return response;
    }

    public void deleteShootingTypeOfStudio(String id) {
        LOGGER.info("Delete shooting type of studio with id: {}", id);
        //check shooting type is exist or not?
        ShootingType shootingType = shootingTypeRepository.findById(id).orElseThrow(() -> new CustomValidationException(Collections.singletonList("Shooting type not found")));
        shootingTypeRepository.delete(shootingType);
    }


}
