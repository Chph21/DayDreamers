package com.example.daydreamer.controller;

import com.example.daydreamer.model._ResponseModel.MetaDataDTO;
import com.example.daydreamer.model.payment.PaymentRequest;
import com.example.daydreamer.model.payment.PaymentResponse;
import com.example.daydreamer.service.PaymentService;
import com.example.daydreamer.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/payments")
@AllArgsConstructor
@Validated
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/search")
    public ResponseEntity<?> searchPayments(
            @RequestParam(required = false) String bookingId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        List<PaymentResponse> result = paymentService.searchPayments(bookingId, status, page, limit);
        return ResponseUtil.getCollection(
                result,
                HttpStatus.OK,
                "Search results fetched successfully",
                new MetaDataDTO(page < result.size(), page > 1, limit, result.size(), page)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<PaymentResponse> result = paymentService.findAll(page, limit);
        return ResponseUtil.getCollection(
                result,
                HttpStatus.OK,
                "Objects results fetched successfully",
                new MetaDataDTO(page < result.size(), page > 1, limit, result.size(), page)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        PaymentResponse result = paymentService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<?> update(@PathVariable String id,
                                    @RequestParam(name = "payosOrderId") String payosOrderId) {
        PaymentResponse result = paymentService.updatePayment(id, payosOrderId);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody PaymentRequest paymentRequest) {
        PaymentResponse result = paymentService.save(paymentRequest);
        result = paymentService.creatPaymentLink(result);
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "Object created successfully");
    }

    @GetMapping({"/success/{id}", "/cancel/{id}"})
    public ResponseEntity<?> updatePayment(@PathVariable(name = "id") String paymentId,
                                            @RequestParam(name = "code") String code,
                                            @RequestParam(name = "id") String id,
                                            @RequestParam(name = "cancel") boolean cancel,
                                            @RequestParam(name = "status") String status,
                                            @RequestParam(name = "orderCode") String orderCode) {
        PaymentResponse result = paymentService.updatePayment(paymentId, orderCode);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("https://lensgo.netlify.app/booking/confirmation/" + paymentId + "/status/" + status));
        return new ResponseEntity<>(result, headers, HttpStatus.FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        paymentService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}