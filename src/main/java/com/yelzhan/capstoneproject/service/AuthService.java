package com.yelzhan.capstoneproject.service;

import com.yelzhan.capstoneproject.model.dto.request.auth.RegistrationRequest;

public interface AuthService {

    void register(RegistrationRequest registrationRequest);
}
