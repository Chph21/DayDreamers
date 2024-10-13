package com.example.daydreamer.repository;

import com.example.daydreamer.entity.StudioConcept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StudioConceptRepository extends JpaRepository<StudioConcept, String>, JpaSpecificationExecutor<StudioConcept> {
}