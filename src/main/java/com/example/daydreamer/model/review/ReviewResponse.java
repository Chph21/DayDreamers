package com.example.daydreamer.model.review;

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
public class ReviewResponse extends GenericResponse {

    private String id;

    private String bookingId;

    private String studioId;

    private String content;

    private Integer rating;

}


