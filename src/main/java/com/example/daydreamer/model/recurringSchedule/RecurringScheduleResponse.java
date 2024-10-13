package com.example.daydreamer.model.recurringSchedule;

import com.example.daydreamer.model._ResponseModel.GenericResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RecurringScheduleResponse extends GenericResponse {
    private String id;

    private String studioId;

    private String dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private String status;
}