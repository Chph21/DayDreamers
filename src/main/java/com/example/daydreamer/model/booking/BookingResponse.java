package com.example.daydreamer.model.booking;

import com.example.daydreamer.model._ResponseModel.GenericResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BookingResponse extends GenericResponse {

    private String id;

    private String studioConceptId;

    private String comboId;

    private String accountId;

    private String studioId;

    private List<String> reviewIds;

    private List<String> paymentIds;

    private Double price;

    private LocalDateTime startTime;

    private Integer duration;

    private LocalDate dateOfPhotoshoot;

    private String meetingLocation;

    private String additionalInfo;

    private String photosLink;
}
