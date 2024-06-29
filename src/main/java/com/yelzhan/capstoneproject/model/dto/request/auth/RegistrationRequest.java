package com.yelzhan.capstoneproject.model.dto.request.auth;

import com.yelzhan.capstoneproject.model.dto.request.CreateRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegistrationRequest implements CreateRequest {

    @NotBlank(message = "Firstname must not be blank")
    @Size(min = 2, max = 64, message = "Firstname must be between 2 and 64 characters long")
    private String firstName;

    @NotBlank(message = "Lastname must not be blank")
    @Size(min = 2, max = 64, message = "Lastname must be between 2 and 64 characters long")
    private String lastName;

    private LocalDate dateOfBirth;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email must not be blank")
    @Size(max = 255, message = "Email must contain at most 255 characters")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters long")
    private String password;

    @NotBlank(message = "Matching password must not be blank")
    @Size(min = 8, max = 255, message = "Matching password must be between 8 and 255 characters long")
    private String matchingPassword;
}
