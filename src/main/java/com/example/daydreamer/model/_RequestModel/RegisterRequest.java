package com.example.daydreamer.model._RequestModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String studioId;

    @NotNull(message = "can not be null")
    @NotBlank(message = "can not be blank")
    @Pattern(regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "invalid email format")
    @Schema(example = "user@example.com")
    private String email;

    @NotNull(message = "can not be null")
    @NotBlank(message = "can not be blank")
    @Pattern(regexp = "^\\+84\\d{9}$", message = "invalid phone number format")
    private String phone;

    @NotNull(message = "can not be null")
    @NotBlank(message = "can not be blank")
    private String password;

    @NotNull(message = "Full Name cannot be null")
    @NotBlank(message = "Full Name cannot be blank")
    private String fullName;

    private String address;

    private LocalDate dob;

    private String gender;

    @NotNull(message = "Nationality cannot be null")
    @NotBlank(message = "Nationality cannot be blank")
    private String nationality;

    private String instagram;

    private String status;
}
