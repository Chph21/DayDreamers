package com.example.daydreamer.model._ResponseModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsOTPResponse {
    private String phone;
    private String status;
    private String message;
}