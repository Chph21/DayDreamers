package com.example.daydreamer.model.closedDay;

import com.example.daydreamer.model._ResponseModel.GenericResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClosedDayResponse extends GenericResponse {
    private String id;

    private String studioId;

    private LocalDate closedDate;

    private String status;
}