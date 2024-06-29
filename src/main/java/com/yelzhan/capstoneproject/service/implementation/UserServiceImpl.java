package com.yelzhan.capstoneproject.service.implementation;

import com.yelzhan.capstoneproject.exception.PasswordMismatchException;
import com.yelzhan.capstoneproject.exception.ResourceAlreadyExistException;
import com.yelzhan.capstoneproject.exception.ResourceNotFoundException;
import com.yelzhan.capstoneproject.model.dto.request.Validatable;
import com.yelzhan.capstoneproject.model.dto.request.auth.RegistrationRequest;
import com.yelzhan.capstoneproject.model.dto.request.user.ChangePasswordRequest;
import com.yelzhan.capstoneproject.model.dto.request.user.UserUpdateRequest;
import com.yelzhan.capstoneproject.model.entity.User;
import com.yelzhan.capstoneproject.repository.UserRepository;
import com.yelzhan.capstoneproject.service.RoleService;
import com.yelzhan.capstoneproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static com.yelzhan.capstoneproject.service.Page.getPageable;
import static com.yelzhan.capstoneproject.service.implementation.AuthServiceImpl.DEFAULT_ROLE;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, Validatable<RegistrationRequest> {

    public static final String ADMIN_ROLE = "ROLE_ADMIN";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;


    @Override
    public User fetchUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));
    }

    @Override
    public boolean changePassword(String email, ChangePasswordRequest updateRequest) {

        if (!userRepository.existsByEmail(email)) {
            return false;
        }
        User userFromDatabase = fetchUserByEmail(email);

        if (isChangePasswordRequestValid(updateRequest, userFromDatabase)) {
            userFromDatabase.setPassword(passwordEncoder.encode(updateRequest.getNewPassword()));
            userRepository.save(userFromDatabase);
            return true;
        }
        return false;

    }

    @Override
    public Page<User> fetchAll(Optional<Integer> page, Optional<Integer> size, Optional<String> sort) {
        Pageable pageable = getPageable(page, size, sort);
        return userRepository.findAll(pageable);
    }

    @Override
    public void activateUser(Long id) {
        User user = fetchById(id);
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void deActivateUser(Long id) {
        User user = fetchById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public User fetchById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id 3 not found"));
    }

    @Override
    public boolean isActive(Long id) {
        return userRepository.findByIdAndIsActive(id);
    }

    @Override
    public void registerAdmin(RegistrationRequest registrationRequest) {

        checkExistenceForCreation(registrationRequest);
        passwordCheck(registrationRequest);

        var entity = User.builder()
                .firstName(registrationRequest.getFirstName().trim())
                .lastName(registrationRequest.getLastName().trim())
                .email(registrationRequest.getEmail().trim())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .roles(roleService.fetchByRoleNames(DEFAULT_ROLE, ADMIN_ROLE))
                .dateOfBirth(registrationRequest.getDateOfBirth())
                .registrationDate(LocalDate.now())
                .isActive(true)
                .build();

        userRepository.save(entity);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean update(String email, UserUpdateRequest updateRequest) {

        if (!userRepository.existsByEmail(email)) {
            return false;
        }
        User userFromDatabase = fetchUserByEmail(email);

        if (Objects.nonNull(updateRequest.getFirstName()) &&
                !updateRequest.getFirstName().equalsIgnoreCase(EMPTY_STRING)) {
            userFromDatabase.setFirstName(updateRequest.getFirstName());
        }
        if (Objects.nonNull(updateRequest.getLastName()) &&
                !updateRequest.getLastName().equalsIgnoreCase(EMPTY_STRING)) {
            userFromDatabase.setLastName(updateRequest.getLastName());
        }
        if (Objects.nonNull(updateRequest.getPhoneNumber()) &&
                !updateRequest.getPhoneNumber().equalsIgnoreCase(EMPTY_STRING)) {
            userFromDatabase.setPhoneNumber(updateRequest.getPhoneNumber());
        }
        if (Objects.nonNull(updateRequest.getDateOfBirth()) &&
                !updateRequest.getDateOfBirth().isAfter(LocalDate.now())) {
            userFromDatabase.setDateOfBirth(updateRequest.getDateOfBirth());
        }
        userRepository.save(userFromDatabase);
        return true;
    }

    @Override
    public void checkExistenceForCreation(RegistrationRequest request) throws ResourceAlreadyExistException {
        String email = request.getEmail().trim();
        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistException("Email " + email + " is already taken");
        }
    }

    private boolean isChangePasswordRequestValid(ChangePasswordRequest updateRequest, User userFromDatabase) {
        if (!isCurrentPasswordValid(updateRequest.getCurrentPassword(), userFromDatabase.getPassword())) {
            throw new PasswordMismatchException("Current password does not match");
        }
        if (!isNewPasswordValid(updateRequest) || !isMatchingPasswordValid(updateRequest)) {
            return false;
        }
        if (!updateRequest.getNewPassword().equals(updateRequest.getMatchingPassword())) {
            throw new PasswordMismatchException("New and matching password does not match");
        }
        return true;
    }

    private boolean isCurrentPasswordValid(String password, String encodedPassword) {
        if (Objects.nonNull(password) && !password.equals(EMPTY_STRING)) {
            return passwordEncoder.matches(password, encodedPassword);
        }
        return false;
    }

    private boolean isNewPasswordValid(ChangePasswordRequest request) {
        return Objects.nonNull(request.getNewPassword()) &&
                !request.getNewPassword().equals(EMPTY_STRING);
    }

    private boolean isMatchingPasswordValid(ChangePasswordRequest request) {
        return Objects.nonNull(request.getMatchingPassword()) &&
                !request.getMatchingPassword().equals(EMPTY_STRING);
    }

    private void passwordCheck(RegistrationRequest request) throws PasswordMismatchException {
        if (!request.getPassword().trim().equals(request.getMatchingPassword().trim())) {
            throw new PasswordMismatchException("Password does not match");
        }
    }
}
