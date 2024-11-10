package com.example.daydreamer.repository;

import com.example.daydreamer.entity.ShootingType;
import com.example.daydreamer.entity.Studio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShootingTypeRepository extends JpaRepository<ShootingType, String> {
    List<ShootingType> findAllByStudio(Studio studio);
    ShootingType findShootingTypeById(String id);
}
