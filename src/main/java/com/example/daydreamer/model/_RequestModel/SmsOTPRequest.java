package com.example.daydreamer.model._RequestModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsOTPRequest {
    @NotNull(message = "can not be null")
    @NotBlank(message = "can not be blank")
    @Pattern(regexp = "^\\+84\\d{9}$", message = "invalid phone number format")
    private String phone;

    private String otp;
}
