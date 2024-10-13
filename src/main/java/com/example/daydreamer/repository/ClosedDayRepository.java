package com.example.daydreamer.repository;

import com.example.daydreamer.entity.ClosedDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClosedDayRepository extends JpaRepository<ClosedDay, String>, JpaSpecificationExecutor<ClosedDay> {
}