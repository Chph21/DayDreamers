package com.example.daydreamer.repository;

import com.example.daydreamer.entity.Combo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboRepository extends JpaRepository<Combo, String>, JpaSpecificationExecutor<Combo> {

}
