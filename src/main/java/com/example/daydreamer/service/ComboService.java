package com.example.daydreamer.service;

import com.example.daydreamer.entity.Combo;
import com.example.daydreamer.entity.Booking;
import com.example.daydreamer.entity.Studio;
import com.example.daydreamer.model.combo.ComboRequest;
import com.example.daydreamer.model.combo.ComboResponse;
import com.example.daydreamer.repository.BookingRepository;
import com.example.daydreamer.repository.ComboRepository;
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
public class ComboService {
    private final Logger LOGGER = LoggerFactory.getLogger(ComboService.class);
    private final ComboRepository comboRepository;
    private final BookingRepository bookingRepository;
    private final StudioRepository studioRepository;

    public List<ComboResponse> searchCombos(String studioId,
                                            Integer editedPhotos,
                                            Integer downloadablePhotos,
                                            Integer duration,
                                            Double price,
                                            String status,
                                            int page,
                                            int limit) {
        LOGGER.info("Searching combos with dynamic criteria");

        Specification<Combo> spec = buildSpecification(studioId, editedPhotos, downloadablePhotos, duration, price, status);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Combo> combos = comboRepository.findAll(spec, pageable);

        return combos.stream()
                .map(this::comboResponseGenerator)
                .collect(Collectors.toList());
    }

    private Specification<Combo> buildSpecification(String studioId,
                                                      Integer editedPhotos,
                                                      Integer downloadablePhotos,
                                                      Integer duration,
                                                      Double price,
                                                      String status) {
        Specification<Combo> spec = Specification.where(null);

        spec = GenericSpecification.addSpecification(spec, studioId, "studio.id", "equal");
        spec = GenericSpecification.addSpecification(spec, editedPhotos, "editedPhotos", "equal");
        spec = GenericSpecification.addSpecification(spec, downloadablePhotos, "downloadablePhotos", "equal");
        spec = GenericSpecification.addSpecification(spec, duration, "duration", "equal");
        spec = GenericSpecification.addSpecification(spec, price, "price", "equal");
        spec = GenericSpecification.addSpecification(spec, status, "status", "equal");

        return spec;
    }

    public List<ComboResponse> findAll(int page, int limit) {
        LOGGER.info("Find all combos");
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Combo> combos = comboRepository.findAll(pageable);
        if (combos.isEmpty()) {
            LOGGER.warn("No combos were found!");
        }

        return combos.stream()
                .map(this::comboResponseGenerator)
                .collect(Collectors.toList());
    }

    public ComboResponse findById(String id) {
        LOGGER.info("Find combo with id " + id);
        Optional<Combo> combo = comboRepository.findById(id);
        if (combo.isEmpty()) {
            LOGGER.warn("No combo was found!");
            return null;
        }
        return combo.map(this::comboResponseGenerator).get();
    }

    public ComboResponse save(ComboRequest comboRequest) {
        Combo combo;
        Optional<Studio> studio = studioRepository.findById(comboRequest.getStudioId());
        if (studio.isEmpty()) {
            throw new CustomValidationException(List.of("No studio was found!"));
        }

        if (comboRequest.getId() != null) {
            LOGGER.info("Update combo with id " + comboRequest.getId());
            checkExist(comboRequest.getId());
            combo = comboRepository.findById(comboRequest.getId()).get();
        } else {
            LOGGER.info("Create new combo");
            combo = new Combo();
        }

        combo.setStudio(studio.get());
        combo.setPrice(comboRequest.getPrice());
        combo.setEditedPhotos(comboRequest.getEditedPhotos());
        combo.setDownloadablePhotos(comboRequest.getDownloadablePhotos());
        combo.setDuration(comboRequest.getDuration());
        combo.setStatus(comboRequest.getStatus());

        comboRepository.save(combo);

        return comboResponseGenerator(combo);
    }

    public void delete(String id) {
        if (id != null) {
            LOGGER.info("Delete combo with id " + id);
            checkExist(id);
            Combo combo = comboRepository.findById(id).get();
            comboRepository.delete(combo);
        }
    }

    private ComboResponse comboResponseGenerator(Combo combo) {
        return ResponseUtil.generateResponse(combo, ComboResponse.class);
    }

    private void checkExist(String id) {
        if (comboRepository.findById(id).isEmpty()) {
            LOGGER.error("No combo was found!");
            throw new CustomValidationException(List.of("No combo was found!"));
        }
    }
}
