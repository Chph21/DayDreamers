package com.example.daydreamer.repository;

import com.example.daydreamer.entity.RecurringSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RecurringScheduleRepository extends JpaRepository<RecurringSchedule, String>, JpaSpecificationExecutor<RecurringSchedule> {
}