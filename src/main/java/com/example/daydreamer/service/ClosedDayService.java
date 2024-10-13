package com.example.daydreamer.service;

import com.example.daydreamer.entity.ClosedDay;
import com.example.daydreamer.entity.Studio;
import com.example.daydreamer.model.closedDay.ClosedDayRequest;
import com.example.daydreamer.model.closedDay.ClosedDayResponse;
import com.example.daydreamer.repository.ClosedDayRepository;
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
public class ClosedDayService {
    private final Logger LOGGER = LoggerFactory.getLogger(ClosedDayService.class);
    private final ClosedDayRepository closedDayRepository;
    private final StudioRepository studioRepository;

    public List<ClosedDayResponse> searchClosedDays(String studioId, String status, int page, int limit) {
        LOGGER.info("Searching closed days with dynamic criteria");

        Specification<ClosedDay> spec = buildSpecification(studioId, status);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<ClosedDay> closedDayPage = closedDayRepository.findAll(spec, pageable);

        return closedDayPage.stream()
                .map(this::closedDayResponseGenerator)
                .collect(Collectors.toList());
    }

    private Specification<ClosedDay> buildSpecification(String studioId, String status) {
        Specification<ClosedDay> spec = Specification.where(null);

        spec = GenericSpecification.addSpecification(spec, studioId, "studio.id", "equal");
        spec = GenericSpecification.addSpecification(spec, status, "status", "equal");

        return spec;
    }

    public List<ClosedDayResponse> findAll(int page, int limit) {
        LOGGER.info("Find all closed days");
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<ClosedDay> closedDayPage = closedDayRepository.findAll(pageable);
        if (closedDayPage.isEmpty()) {
            LOGGER.warn("No closed days were found!");
        }

        return closedDayPage.stream()
                .map(this::closedDayResponseGenerator)
                .collect(Collectors.toList());
    }

    public ClosedDayResponse findById(String id) {
        LOGGER.info("Find closed day with id " + id);
        Optional<ClosedDay> closedDay = closedDayRepository.findById(id);
        if (closedDay.isEmpty()) {
            LOGGER.warn("No closed day was found!");
            return null;
        }
        return closedDay.map(this::closedDayResponseGenerator).get();
    }

    public ClosedDayResponse save(ClosedDayRequest closedDayRequest) {
        ClosedDay closedDay;
        Optional<Studio> studio = studioRepository.findById(closedDayRequest.getStudioId());
        if (studio.isEmpty()) {
            throw new CustomValidationException(List.of("No studio was found!"));
        }

        if (closedDayRequest.getId() != null) {
            LOGGER.info("Update closed day with id " + closedDayRequest.getId());
            checkExist(closedDayRequest.getId());
            closedDay = closedDayRepository.findById(closedDayRequest.getId()).get();
        } else {
            LOGGER.info("Create new closed day");
            closedDay = new ClosedDay();
        }

        closedDay.setStudio(studio.get());
        closedDay.setClosedDate(closedDayRequest.getClosedDate());
        closedDay.setStatus(closedDayRequest.getStatus());

        closedDayRepository.save(closedDay);

        return closedDayResponseGenerator(closedDay);
    }

    public void delete(String id) {
        if (id != null) {
            LOGGER.info("Delete closed day with id " + id);
            checkExist(id);
            ClosedDay closedDay = closedDayRepository.findById(id).get();
            closedDayRepository.delete(closedDay);
        }
    }

    public ClosedDayResponse closedDayResponseGenerator(ClosedDay closedDay) {
        return ResponseUtil.generateResponse(closedDay, ClosedDayResponse.class);
    }

    private void checkExist(String id) {
        if (closedDayRepository.findById(id).isEmpty()) {
            LOGGER.error("No closed day was found!");
            throw new CustomValidationException(List.of("No closed day was found!"));
        }
    }
}