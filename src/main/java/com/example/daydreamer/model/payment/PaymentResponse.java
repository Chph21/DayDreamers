package com.example.daydreamer.model.payment;

import com.example.daydreamer.model._ResponseModel.GenericResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaymentResponse extends GenericResponse {
    private String id;

    private String bookingId;

    private Double amount;

    private LocalDateTime paymentDate;

    private String status;
}