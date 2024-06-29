package com.yelzhan.capstoneproject.service.authService;

import com.yelzhan.capstoneproject.exception.PasswordMismatchException;
import com.yelzhan.capstoneproject.exception.ResourceAlreadyExistException;
import com.yelzhan.capstoneproject.model.dto.request.auth.RegistrationRequest;
import com.yelzhan.capstoneproject.model.entity.User;
import com.yelzhan.capstoneproject.repository.UserRepository;
import com.yelzhan.capstoneproject.service.implementation.AuthServiceImpl;
import com.yelzhan.capstoneproject.service.implementation.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static com.yelzhan.capstoneproject.service.implementation.AuthServiceImpl.DEFAULT_ROLE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleServiceImpl roleService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_ValidRequest() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setFirstName("John");
        registrationRequest.setLastName("Doe");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("password");
        registrationRequest.setMatchingPassword("password");
        registrationRequest.setDateOfBirth(LocalDate.of(1990, 1, 1));

        when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("encodedPassword");

        assertDoesNotThrow(() -> authService.register(registrationRequest));

        verify(userRepository, times(1)).existsByEmail(registrationRequest.getEmail());
        verify(passwordEncoder, times(1)).encode(registrationRequest.getPassword());
        verify(roleService, times(1)).fetchByRoleNames(DEFAULT_ROLE);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("test@example.com");

        when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(true);

        assertThrows(ResourceAlreadyExistException.class, () -> authService.register(registrationRequest));

        verify(userRepository, times(1)).existsByEmail(registrationRequest.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(roleService, never()).fetchByRoleNames(any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testPasswordCheck_PasswordMismatch() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setPassword("password1");
        registrationRequest.setMatchingPassword("password2");

        assertThrows(PasswordMismatchException.class, () -> authService.passwordCheck(registrationRequest));
    }

    @Test
    void testPasswordCheck_PasswordMatch() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setPassword("password");
        registrationRequest.setMatchingPassword("password");

        assertDoesNotThrow(() -> authService.passwordCheck(registrationRequest));
    }
}
