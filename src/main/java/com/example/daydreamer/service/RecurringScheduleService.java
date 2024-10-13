package com.example.daydreamer.service;

import com.example.daydreamer.entity.RecurringSchedule;
import com.example.daydreamer.entity.Studio;
import com.example.daydreamer.model.recurringSchedule.RecurringScheduleRequest;
import com.example.daydreamer.model.recurringSchedule.RecurringScheduleResponse;
import com.example.daydreamer.repository.RecurringScheduleRepository;
import com.example.daydreamer.repository.StudioRepository;
import com.example.daydreamer.specification.GenericSpecification;
import com.example.daydreamer.utils.CustomValidationException;
import com.example.daydreamer.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecurringScheduleService {
    private final Logger LOGGER = LoggerFactory.getLogger(RecurringScheduleService.class);
    private final RecurringScheduleRepository recurringScheduleRepository;
    private final StudioRepository studioRepository;

    public List<RecurringScheduleResponse> searchRecurringSchedules(String studioId, String dayOfWeek, String status, int page, int limit) {
        LOGGER.info("Searching recurring schedules with dynamic criteria");

        Specification<RecurringSchedule> spec = buildSpecification(studioId, dayOfWeek, status);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<RecurringSchedule> recurringSchedulePage = recurringScheduleRepository.findAll(spec, pageable);

        return recurringSchedulePage.stream()
                .map(this::recurringScheduleResponseGenerator)
                .collect(Collectors.toList());
    }

    private Specification<RecurringSchedule> buildSpecification(String studioId, String dayOfWeek, String status) {
        Specification<RecurringSchedule> spec = Specification.where(null);

        spec = GenericSpecification.addSpecification(spec, studioId, "studio.id", "equal");
        spec = GenericSpecification.addSpecification(spec, dayOfWeek, "dayOfWeek", "equal");
        spec = GenericSpecification.addSpecification(spec, status, "status", "equal");

        return spec;
    }

    public List<RecurringScheduleResponse> findAll(int page, int limit) {
        LOGGER.info("Find all recurring schedules");
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<RecurringSchedule> recurringSchedulePage = recurringScheduleRepository.findAll(pageable);
        if (recurringSchedulePage.isEmpty()) {
            LOGGER.warn("No recurring schedules were found!");
        }

        return recurringSchedulePage.stream()
                .map(this::recurringScheduleResponseGenerator)
                .collect(Collectors.toList());
    }

    public RecurringScheduleResponse findById(String id) {
        LOGGER.info("Find recurring schedule with id " + id);
        Optional<RecurringSchedule> recurringSchedule = recurringScheduleRepository.findById(id);
        if (recurringSchedule.isEmpty()) {
            LOGGER.warn("No recurring schedule was found!");
            return null;
        }
        return recurringSchedule.map(this::recurringScheduleResponseGenerator).get();
    }

    public RecurringScheduleResponse save(RecurringScheduleRequest recurringScheduleRequest) {
        RecurringSchedule recurringSchedule;
        Optional<Studio> studio = studioRepository.findById(recurringScheduleRequest.getStudioId());
        if (studio.isEmpty()) {
            throw new CustomValidationException(List.of("No studio was found!"));
        }

        if (recurringScheduleRequest.getId() != null) {
            LOGGER.info("Update recurring schedule with id " + recurringScheduleRequest.getId());
            checkExist(recurringScheduleRequest.getId());
            recurringSchedule = recurringScheduleRepository.findById(recurringScheduleRequest.getId()).get();
        } else {
            LOGGER.info("Create new recurring schedule");
            recurringSchedule = new RecurringSchedule();
        }

        recurringSchedule.setStudio(studio.get());
        recurringSchedule.setDayOfWeek(recurringScheduleRequest.getDayOfWeek());
        recurringSchedule.setStartTime(recurringScheduleRequest.getStartTime());
        recurringSchedule.setEndTime(recurringScheduleRequest.getEndTime());
        recurringSchedule.setStatus(recurringScheduleRequest.getStatus());

        recurringScheduleRepository.save(recurringSchedule);

        return recurringScheduleResponseGenerator(recurringSchedule);
    }

    public void delete(String id) {
        if (id != null) {
            LOGGER.info("Delete recurring schedule with id " + id);
            checkExist(id);
            RecurringSchedule recurringSchedule = recurringScheduleRepository.findById(id).get();
            recurringScheduleRepository.delete(recurringSchedule);
        }
    }

    public RecurringScheduleResponse recurringScheduleResponseGenerator(RecurringSchedule recurringSchedule) {
        return ResponseUtil.generateResponse(recurringSchedule, RecurringScheduleResponse.class);
    }

    private void checkExist(String id) {
        if (recurringScheduleRepository.findById(id).isEmpty()) {
            LOGGER.error("No recurring schedule was found!");
            throw new CustomValidationException(List.of("No recurring schedule was found!"));
        }
    }
}