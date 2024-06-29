package com.yelzhan.capstoneproject.controller;

import com.yelzhan.capstoneproject.model.dto.request.auth.LoginRequest;
import com.yelzhan.capstoneproject.model.dto.request.auth.RegistrationRequest;
import com.yelzhan.capstoneproject.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.yelzhan.capstoneproject.config.ApplicationConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignupForm() {
        String expectedView = "signup";

        String result = authController.signupForm(model);

        verify(model).addAttribute(eq(PAGE_TITLE.getValue()), eq(SIGNUP.getValue()));
        verify(model).addAttribute(eq("request"), any(RegistrationRequest.class));
        verifyNoMoreInteractions(model);

        assertEquals(expectedView, result);
    }

    @Test
    void testSignup() {
        String expectedView = "redirect:login?registration=success";
        RegistrationRequest registrationRequest = new RegistrationRequest();

        when(bindingResult.hasErrors()).thenReturn(false);

        String result = authController.signup(registrationRequest, bindingResult, redirectAttributes);

        verify(authService).register(registrationRequest);
        verifyNoMoreInteractions(authService);

        assertEquals(expectedView, result);
    }

    @Test
    void testSignupWithErrors() {
        String expectedView = "redirect:signup";
        RegistrationRequest registrationRequest = new RegistrationRequest();

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("objectName", "field", "message")));

        String result = authController.signup(registrationRequest, bindingResult, redirectAttributes);

        verify(bindingResult).hasErrors();
        verify(bindingResult).getFieldErrors();
        verify(redirectAttributes).addAttribute(eq("failed"), eq(true));
        verify(redirectAttributes).addAttribute(eq("field"), argThat(argument -> argument.equals("message")));
        verifyNoMoreInteractions(bindingResult, redirectAttributes);

        assertEquals(expectedView, result);
    }

    @Test
    void testLoginForm() {
        AuthService authService = mock(AuthService.class);
        AuthController authController = new AuthController(authService);
        Model model = mock(Model.class);

        String expectedView = "login";

        String result = authController.loginForm(model);

        verify(model).addAttribute(eq(PAGE_TITLE.getValue()), eq(LOGIN.getValue()));
        verify(model).addAttribute(eq("request"), any(LoginRequest.class));
        verifyNoMoreInteractions(model);

        assertEquals(expectedView, result);
    }

}
