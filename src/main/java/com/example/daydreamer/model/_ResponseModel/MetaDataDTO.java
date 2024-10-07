package com.example.daydreamer.model._ResponseModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaDataDTO {
    private boolean hasNextPage;
    private boolean hasPrevPage;
    private int limit;
    private int total;
    private int page;
}