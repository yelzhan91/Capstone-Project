package com.yelzhan.capstoneproject.model.dto.request.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserUpdateRequest {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dateOfBirth;

}
