package com.example.daydreamer.model.combo;

import com.example.daydreamer.model._ResponseModel.GenericResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComboResponse extends GenericResponse {

    private String id;

    private String studioId;

    private List<String> bookingIds;

    private Integer editedPhotos;

    private Integer downloadablePhotos;

    private Integer duration;

    private Double price;
}
