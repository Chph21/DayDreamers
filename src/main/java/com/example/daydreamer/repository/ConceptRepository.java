package com.example.daydreamer.repository;

import com.example.daydreamer.entity.Concept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConceptRepository extends JpaRepository<Concept, String>, JpaSpecificationExecutor<Concept> {
}
