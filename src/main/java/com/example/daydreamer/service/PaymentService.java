package com.example.daydreamer.service;

import com.example.daydreamer.entity.Payment;
import com.example.daydreamer.entity.Booking;
import com.example.daydreamer.model.payment.PaymentRequest;
import com.example.daydreamer.model.payment.PaymentResponse;
import com.example.daydreamer.repository.PaymentRepository;
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
public class PaymentService {
    private final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public List<PaymentResponse> searchPayments(String bookingId, String status, int page, int limit) {
        LOGGER.info("Searching payments with dynamic criteria");

        Specification<Payment> spec = buildSpecification(bookingId, status);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Payment> paymentPage = paymentRepository.findAll(spec, pageable);

        return paymentPage.stream()
                .map(this::paymentResponseGenerator)
                .collect(Collectors.toList());
    }

    private Specification<Payment> buildSpecification(String bookingId, String status) {
        Specification<Payment> spec = Specification.where(null);

        spec = GenericSpecification.addSpecification(spec, bookingId, "booking.id", "equal");
        spec = GenericSpecification.addSpecification(spec, status, "status", "equal");

        return spec;
    }

    public List<PaymentResponse> findAll(int page, int limit) {
        LOGGER.info("Find all payments");
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Payment> paymentPage = paymentRepository.findAll(pageable);
        if (paymentPage.isEmpty()) {
            LOGGER.warn("No payments were found!");
        }

        return paymentPage.stream()
                .map(this::paymentResponseGenerator)
                .collect(Collectors.toList());
    }

    public PaymentResponse findById(String id) {
        LOGGER.info("Find payment with id " + id);
        Optional<Payment> payment = paymentRepository.findById(id);
        if (payment.isEmpty()) {
            LOGGER.warn("No payment was found!");
            return null;
        }
        return payment.map(this::paymentResponseGenerator).get();
    }

    public PaymentResponse save(PaymentRequest paymentRequest) {
        Payment payment;
        Optional<Booking> booking = bookingRepository.findById(paymentRequest.getBookingId());
        if (booking.isEmpty()) {
            throw new CustomValidationException(List.of("No booking was found!"));
        }

        if (paymentRequest.getId() != null) {
            LOGGER.info("Update payment with id " + paymentRequest.getId());
            checkExist(paymentRequest.getId());
            payment = paymentRepository.findById(paymentRequest.getId()).get();
        } else {
            LOGGER.info("Create new payment");
            payment = new Payment();
        }

        payment.setBooking(booking.get());
        payment.setAmount(paymentRequest.getAmount());
        payment.setPaymentDate(paymentRequest.getPaymentDate());
        payment.setStatus(paymentRequest.getStatus());

        paymentRepository.save(payment);

        return paymentResponseGenerator(payment);
    }

    public void delete(String id) {
        if (id != null) {
            LOGGER.info("Delete payment with id " + id);
            checkExist(id);
            Payment payment = paymentRepository.findById(id).get();
            paymentRepository.delete(payment);
        }
    }

    public PaymentResponse paymentResponseGenerator(Payment payment) {
        return ResponseUtil.generateResponse(payment, PaymentResponse.class);
    }

    private void checkExist(String id) {
        if (paymentRepository.findById(id).isEmpty()) {
            LOGGER.error("No payment was found!");
            throw new CustomValidationException(List.of("No payment was found!"));
        }
    }
}