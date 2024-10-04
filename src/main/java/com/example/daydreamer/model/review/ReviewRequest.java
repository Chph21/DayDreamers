package com.example.daydreamer.model.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {

    private String id;

    @NotNull(message = "Booking ID cannot be null")
    @NotBlank(message = "Booking ID cannot be blank")
    private String bookingId;

    @NotNull(message = "Studio ID cannot be null")
    @NotBlank(message = "Studio ID cannot be blank")
    private String studioId;

    private String content;

    @NotNull(message = "Rating cannot be null")
    private Integer rating;

    private String status;
}
