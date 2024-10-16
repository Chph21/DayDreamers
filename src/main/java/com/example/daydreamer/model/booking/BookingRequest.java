package com.example.daydreamer.model.booking;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    private String id;

    private String conceptId;

    private String comboId;

    @NotNull(message = "Account ID can not be null")
    @NotBlank(message = "Account ID can not be blank")
    private String accountId;

    @NotNull(message = "Studio ID can not be null")
    @NotBlank(message = "Studio ID can not be blank")
    private String studioId;

    private String paymentId;

    @NotNull(message = "Price can not be null")
    private Double price;

    @NotNull(message = "Start Time can not be null")
    private LocalDateTime startTime;

    @NotNull(message = "Duration can not be null")
    private Integer duration;

    @NotNull(message = "Date of Photoshoot can not be null")
    private LocalDate dateOfPhotoshoot;

    @NotNull(message = "Meeting Location can not be null")
    private String meetingLocation;

    private String additionalInfo;

    private String photosLink;

    @NotNull(message = "Status can not be null")
    @NotBlank(message = "Status can not be blank")
    private String status;
}
