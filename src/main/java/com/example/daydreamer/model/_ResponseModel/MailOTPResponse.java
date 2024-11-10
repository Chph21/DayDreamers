package com.example.daydreamer.model._ResponseModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailOTPResponse {
    private String email;
    private String status;
    private String message;
}
