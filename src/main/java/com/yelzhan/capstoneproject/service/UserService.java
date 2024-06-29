package com.yelzhan.capstoneproject.service;

import com.yelzhan.capstoneproject.model.dto.request.auth.RegistrationRequest;
import com.yelzhan.capstoneproject.model.dto.request.user.ChangePasswordRequest;
import com.yelzhan.capstoneproject.model.dto.request.user.UserUpdateRequest;
import com.yelzhan.capstoneproject.model.entity.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {

    boolean update(String email, UserUpdateRequest updateRequest);

    User fetchUserByEmail(String email);

    boolean changePassword(String email, ChangePasswordRequest updateRequest);

    Page<User> fetchAll(Optional<Integer> page, Optional<Integer> size, Optional<String> sort);

    void activateUser(Long id);
    void deActivateUser(Long id);

    User fetchById(Long id);

    boolean isActive(Long id);

    void registerAdmin(RegistrationRequest registrationRequest);

    boolean existsByEmail(String email);
}
