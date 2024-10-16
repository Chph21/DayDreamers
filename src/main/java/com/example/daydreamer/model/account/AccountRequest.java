package com.example.daydreamer.model.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    private String id;

    private String studioId;

    private String authId;

    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotNull(message = "Full Name cannot be null")
    @NotBlank(message = "Full Name cannot be blank")
    private String fullName;

    private String address;

    private LocalDate dob;

    private String gender;

    @NotNull(message = "Phone Number cannot be null")
    @NotBlank(message = "Phone Number cannot be blank")
    private String phoneNumber;

    @NotNull(message = "Nationality cannot be null")
    @NotBlank(message = "Nationality cannot be blank")
    private String nationality;

    private String instagram;

    private String avatarLink;

    private String status;

}
