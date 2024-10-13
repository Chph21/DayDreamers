package com.example.daydreamer.model.recurringSchedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecurringScheduleRequest {

    private String id;

    @NotNull(message = "Studio ID cannot be null")
    @NotBlank(message = "Studio ID cannot be blank")
    private String studioId;

    @NotNull(message = "Day of Week cannot be null")
    @NotBlank(message = "Day of Week cannot be blank")
    private String dayOfWeek;

    @NotNull(message = "Start Time cannot be null")
    private LocalTime startTime;

    @NotNull(message = "End Time cannot be null")
    private LocalTime endTime;

    @NotNull(message = "Status cannot be null")
    @NotBlank(message = "Status cannot be blank")
    private String status;
}