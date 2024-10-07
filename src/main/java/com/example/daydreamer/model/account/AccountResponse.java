package com.example.daydreamer.model.account;

import com.example.daydreamer.model._ResponseModel.GenericResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountResponse extends GenericResponse {
    private String id;

    private String studioId;

    private String authId;

    private List<String> bookingIds;

    private String username;

    private String password;

    private String fullName;

    private String address;

    private LocalDate dob;

    private String gender;

    private String phoneNumber;

    private String nationality;

    private String instagram;

    private String avatarLink;

}




