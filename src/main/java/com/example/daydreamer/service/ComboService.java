package com.example.daydreamer.service;

import com.example.daydreamer.entity.Booking;
import com.example.daydreamer.entity.Combo;
import com.example.daydreamer.entity.Studio;
import com.example.daydreamer.model.combo.ComboRequest;
import com.example.daydreamer.model.combo.ComboResponse;
import com.example.daydreamer.repository.BookingRepository;
import com.example.daydreamer.repository.ComboRepository;
import com.example.daydreamer.repository.StudioRepository;
import com.example.daydreamer.specification.ComboSpecification;
import com.example.daydreamer.utils.CustomValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
                                            String status) {
        LOGGER.info("Searching combos with dynamic criteria");

        Specification<Combo> spec = Specification.where(null);

        if (studioId != null && !studioId.isEmpty()) {
            spec = spec.and(ComboSpecification.hasStudioId(studioId));
        }
        if (editedPhotos != null) {
            spec = spec.and(ComboSpecification.hasNumOfEditedPhotos(editedPhotos));
        }
        if (downloadablePhotos != null) {
            spec = spec.and(ComboSpecification.hasNumOfDownloadablePhotos(downloadablePhotos));
        }
        if (duration != null) {
            spec = spec.and(ComboSpecification.hasDuration(duration));
        }
        if (price != null) {
            spec = spec.and(ComboSpecification.hasPrice(price));
        }
        if (status != null && !status.isEmpty()) {
            spec = spec.and(ComboSpecification.hasStatus(status));
        }

        List<Combo> combos = comboRepository.findAll(spec);

        return combos.stream()
                .map(this::comboResponseGenerator)
                .collect(Collectors.toList());
    }

    public List<ComboResponse> findAll() {
        LOGGER.info("Find all booking details");
        List<Combo> combos = comboRepository.findAll();
        if (combos.isEmpty()) {
            LOGGER.warn("No booking details were found!");
        }

        return combos.stream()
                .map(this::comboResponseGenerator)
                .collect(Collectors.toList());
    }

    public ComboResponse findById(String id) {
        LOGGER.info("Find booking detail with id " + id);
        Optional<Combo> combo = comboRepository.findById(id);
        if (combo.isEmpty()) {
            LOGGER.warn("No booking detail was found!");
            return null;
        }
        return combo.map(this::comboResponseGenerator).get();
    }

    public ComboResponse save(ComboRequest comboRequest) {
        Combo combo;
        List<Booking> bookings = bookingRepository.findAllById(comboRequest.getBookingIds());
        Optional<Studio> studio = studioRepository.findById(comboRequest.getStudioId());
        if ((!comboRequest.getBookingIds().isEmpty() && bookings.isEmpty()) || studio.isEmpty()) {
            throw new CustomValidationException(List.of("No bookings or studio was found!"));
        }

        if (comboRequest.getId() != null) {
            LOGGER.info("Update booking detail with id " + comboRequest.getId());
            checkExist(comboRequest.getId());
            combo = comboRepository.findById(comboRequest.getId()).get();
        } else {
            LOGGER.info("Create new booking detail");
            combo = new Combo();
        }

        combo.setBookings(bookings);
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
            LOGGER.info("Delete booking detail with id " + id);
            checkExist(id);
            Combo combo = comboRepository.findById(id).get();
            comboRepository.delete(combo);
        }
    }

    private ComboResponse comboResponseGenerator(Combo combo) {
        ComboResponse comboResponse = new ComboResponse();
        comboResponse.setId(combo.getId());
        comboResponse.setBookingIds(combo.getBookings().stream().map(Booking::getId).toList());
        comboResponse.setStudioId(combo.getStudio().getId());
        comboResponse.setPrice(combo.getPrice());
        comboResponse.setEditedPhotos(combo.getEditedPhotos());
        comboResponse.setDownloadablePhotos(combo.getDownloadablePhotos());
        comboResponse.setDuration(combo.getDuration());
        comboResponse.setStatus(combo.getStatus());
        comboResponse.setCreatedBy(combo.getCreatedBy());
        comboResponse.setCreatedDate(combo.getCreatedDate());
        comboResponse.setUpdatedDate(combo.getUpdatedDate());
        comboResponse.setUpdatedBy(combo.getUpdatedBy());
        return comboResponse;
    }

    private void checkExist(String id) {
        if (comboRepository.findById(id).isEmpty()) {
            LOGGER.error("No booking detail was found!");
            throw new CustomValidationException(List.of("No booking detail was found!"));
        }
    }
}
