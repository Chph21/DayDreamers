package com.example.daydreamer.model.closedDay;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClosedDayRequest {

    private String id;

    @NotNull(message = "Studio ID cannot be null")
    @NotBlank(message = "Studio ID cannot be blank")
    private String studioId;

    @NotNull(message = "Closed Date cannot be null")
    private LocalDate closedDate;

    @NotNull(message = "Status cannot be null")
    @NotBlank(message = "Status cannot be blank")
    private String status;
}