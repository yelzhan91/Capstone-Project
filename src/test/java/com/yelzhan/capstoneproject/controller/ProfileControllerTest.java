package com.yelzhan.capstoneproject.controller;

import com.yelzhan.capstoneproject.model.dto.request.user.ChangePasswordRequest;
import com.yelzhan.capstoneproject.model.dto.request.user.UserUpdateRequest;
import com.yelzhan.capstoneproject.model.entity.User;
import com.yelzhan.capstoneproject.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProfileControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Test
    public void testProfile() {
        MockitoAnnotations.openMocks(this);

        ProfileController profileController = new ProfileController(userService);
        Model model = new ConcurrentModel();

        User user = new User();
        user.setEmail("test@example.com");
        when(userService.fetchUserByEmail("test@example.com")).thenReturn(user);

        Authentication authentication = new TestingAuthenticationToken("test@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String viewName = profileController.profile(model);

        assertEquals("profile", viewName);
        assertEquals(user, model.getAttribute("user"));
        assertEquals("Profile", model.getAttribute("pageTitle"));
    }


    @Test
    public void testEditProfilePostFailure() {
        MockitoAnnotations.openMocks(this);

        ProfileController profileController = new ProfileController(userService);
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setFirstName("John");

        Authentication authentication = new TestingAuthenticationToken("test@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String viewName = profileController.editProfile(updateRequest, redirectAttributes);

        assertEquals("redirect:edit", viewName);
        verify(userService).update("test@example.com", updateRequest);
        verify(redirectAttributes).addAttribute("error", true);
    }

    @Test
    public void testEditProfilePostFail() {
        MockitoAnnotations.openMocks(this);

        ProfileController profileController = new ProfileController(userService);
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setFirstName("John");

        Authentication authentication = new TestingAuthenticationToken("test@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userService.update("test@example.com", updateRequest)).thenReturn(false);

        String viewName = profileController.editProfile(updateRequest, redirectAttributes);

        assertEquals("redirect:edit", viewName);
        verify(redirectAttributes).addAttribute("error", true);
    }

    @Test
    public void testChangePasswordSuccess() {
        MockitoAnnotations.openMocks(this);

        ProfileController profileController = new ProfileController(userService);
        ChangePasswordRequest updateRequest = new ChangePasswordRequest();
        updateRequest.setCurrentPassword("oldPassword");
        updateRequest.setNewPassword("newPassword");
        updateRequest.setMatchingPassword("newPassword");

        Authentication authentication = new TestingAuthenticationToken("test@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String viewName = profileController.changePassword(updateRequest, redirectAttributes);

        assertEquals("redirect:edit", viewName);
        verify(userService).changePassword("test@example.com", updateRequest);
        verify(redirectAttributes).addAttribute("passwordError", true);
    }

    @Test
    public void testChangePasswordFailure() {
        MockitoAnnotations.openMocks(this);

        ProfileController profileController = new ProfileController(userService);
        ChangePasswordRequest updateRequest = new ChangePasswordRequest();
        updateRequest.setCurrentPassword("oldPassword");
        updateRequest.setNewPassword("newPassword");
        updateRequest.setMatchingPassword("newPassword");

        Authentication authentication = new TestingAuthenticationToken("test@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userService.changePassword("test@example.com", updateRequest)).thenReturn(false);

        String viewName = profileController.changePassword(updateRequest, redirectAttributes);

        assertEquals("redirect:edit", viewName);
        verify(redirectAttributes).addAttribute("passwordError", true);
    }

}
