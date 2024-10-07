package com.example.daydreamer.service;

import com.example.daydreamer.entity.*;
import com.example.daydreamer.entity.Booking;
import com.example.daydreamer.model.booking.BookingRequest;
import com.example.daydreamer.model.booking.BookingResponse;
import com.example.daydreamer.repository.*;
import com.example.daydreamer.repository.BookingRepository;
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
public class BookingService {

    private final Logger LOGGER = LoggerFactory.getLogger(BookingService.class);
    private final BookingRepository bookingRepository;
    private final ComboRepository comboRepository;
    private final ConceptRepository conceptRepository;
    private final AccountRepository accountRepository;
    private final ReviewRepository reviewRepository;
    private final PaymentRepository paymentRepository;
    private final StudioRepository studioRepository;

    public List<BookingResponse> searchBookings(String conceptId,
                                                String comboId,
                                                String accountId,
                                                String studioId,
                                                Double price,
                                                String status,
                                                int page,
                                                int limit) {
        LOGGER.info("Searching bookings with dynamic criteria");

        Specification<Booking> spec = buildSpecification(conceptId, comboId, accountId, studioId, price, status);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Booking> bookings = bookingRepository.findAll(spec, pageable);

        return bookings.stream()
                .map(this::bookingResponseGenerator)
                .collect(Collectors.toList());
    }

    private Specification<Booking> buildSpecification(String conceptId,
                                                      String comboId,
                                                      String accountId,
                                                      String studioId,
                                                      Double price,
                                                      String status) {
        Specification<Booking> spec = Specification.where(null);

        spec = GenericSpecification.addSpecification(spec, conceptId, "concept.id", "equal");
        spec = GenericSpecification.addSpecification(spec, comboId, "combo.id", "equal");
        spec = GenericSpecification.addSpecification(spec, accountId, "account.id", "equal");
        spec = GenericSpecification.addSpecification(spec, studioId, "studio.id", "equal");
        spec = GenericSpecification.addSpecification(spec, price, "price", "equal");
        spec = GenericSpecification.addSpecification(spec, status, "status", "equal");

        return spec;
    }

    public List<BookingResponse> findAll(int page, int limit) {
        LOGGER.info("Find all bookings");
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Booking> bookings = bookingRepository.findAll(pageable);
        if (bookings.isEmpty()) {
            LOGGER.warn("No bookings were found!");
        }

        return bookings.stream()
                .map(this::bookingResponseGenerator)
                .collect(Collectors.toList());
    }

    public BookingResponse findById(String id) {
        LOGGER.info("Find booking with id " + id);
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty()) {
            LOGGER.warn("No booking was found!");
            return null;
        }
        return booking.map(this::bookingResponseGenerator).get();
    }

    public BookingResponse save(BookingRequest bookingRequest) {
        Booking booking;
        List<Review> reviews = reviewRepository.findAllById(bookingRequest.getReviewIds());
        Optional<Concept> concept = conceptRepository.findById(bookingRequest.getConceptId());
        Optional<Combo> combo = comboRepository.findById(bookingRequest.getComboId());
        Optional<Account> account = accountRepository.findById(bookingRequest.getAccountId());
        Optional<Studio> studio = studioRepository.findById(bookingRequest.getStudioId());
        Optional<Payment> payment = paymentRepository.findById(bookingRequest.getPaymentId());
        if ((!bookingRequest.getReviewIds().isEmpty() && reviews.isEmpty()) ||
                (!bookingRequest.getConceptId().isEmpty() && concept.isEmpty()) ||
                (!bookingRequest.getComboId().isEmpty() && combo.isEmpty()) ||
                (!bookingRequest.getStudioId().isEmpty() && studio.isEmpty()) ||
                (!bookingRequest.getPaymentId().isEmpty() && payment.isEmpty()) ||
                account.isEmpty() || studio.isEmpty()) {
            throw new CustomValidationException(List.of("Missing attributes!"));
        }

        if (bookingRequest.getId() != null) {
            LOGGER.info("Update booking with id " + bookingRequest.getId());
            checkExist(bookingRequest.getId());
            booking = bookingRepository.findById(bookingRequest.getId()).get();
        } else {
            LOGGER.info("Create new booking");
            booking = new Booking();
        }
        booking.setConcept(concept.orElse(null));
        booking.setCombo(combo.orElse(null));
        booking.setPayment(payment.orElse(null));
        booking.setAccount(account.get());
        booking.setStudio(studio.get());
        booking.setReview(reviews);
        booking.setStartTime(bookingRequest.getStartTime());
        booking.setDateOfPhotoshoot(bookingRequest.getDateOfPhotoshoot());
        booking.setMeetingLocation(bookingRequest.getMeetingLocation());
        booking.setPhotosLink(bookingRequest.getPhotosLink());
        booking.setAdditionalInfo(bookingRequest.getAdditionalInfo());
        booking.setPrice(bookingRequest.getPrice());
        booking.setDuration(bookingRequest.getDuration());
        booking.setStatus(bookingRequest.getStatus());

        bookingRepository.save(booking);

        return bookingResponseGenerator(booking);
    }

    public void delete(String id) {
        if (id != null) {
            LOGGER.info("Delete booking with id " + id);
            checkExist(id);
            Booking booking = bookingRepository.findById(id).get();
            bookingRepository.delete(booking);
        }
    }

    private BookingResponse bookingResponseGenerator(Booking booking) {
        return ResponseUtil.generateResponse(booking, BookingResponse.class);
    }

    private void checkExist(String id) {
        if (bookingRepository.findById(id).isEmpty()) {
            LOGGER.error("No booking was found!");
            throw new CustomValidationException(List.of("No booking was found!"));
        }
    }
}
