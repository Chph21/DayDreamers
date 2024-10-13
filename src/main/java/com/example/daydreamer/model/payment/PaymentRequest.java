package com.example.daydreamer.model.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    private String id;

    @NotNull(message = "Booking ID cannot be null")
    @NotBlank(message = "Booking ID cannot be blank")
    private String bookingId;

    @NotNull(message = "Amount cannot be null")
    private Double amount;

    @NotNull(message = "Payment Date cannot be null")
    private LocalDateTime paymentDate;

    @NotNull(message = "Status cannot be null")
    @NotBlank(message = "Status cannot be blank")
    private String status;
}