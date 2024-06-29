package com.yelzhan.capstoneproject.controller;

import com.yelzhan.capstoneproject.config.security.SecurityUtils;
import com.yelzhan.capstoneproject.exception.PasswordMismatchException;
import com.yelzhan.capstoneproject.model.dto.request.user.ChangePasswordRequest;
import com.yelzhan.capstoneproject.model.dto.request.user.UserUpdateRequest;
import com.yelzhan.capstoneproject.model.entity.User;
import com.yelzhan.capstoneproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.yelzhan.capstoneproject.config.ApplicationConstants.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;


    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public String profile(Model model) {

        final String email = SecurityUtils.getUsername();
        User user = userService.fetchUserByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute(PAGE_TITLE.getValue(), PROFILE.getValue());
        return "profile";
    }

    @GetMapping("/edit")
    @PreAuthorize("hasRole('USER')")
    public String editProfile(Model model) {

        model.addAttribute(PAGE_TITLE.getValue(), EDIT_PROFILE.getValue());
        model.addAttribute("request", new UserUpdateRequest());
        model.addAttribute("passwordRequest", new ChangePasswordRequest());
        return "edit";
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('USER')")
    public String editProfile(@ModelAttribute("request") UserUpdateRequest updateRequest,
                              RedirectAttributes redirectAttributes) {

        final String email = SecurityUtils.getUsername();

        if (!userService.update(email, updateRequest)) {
            redirectAttributes.addAttribute("error", true);
            return "redirect:edit";
        }
        redirectAttributes.addAttribute("update", true);
        return "redirect:../profile";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute("passwordRequest") ChangePasswordRequest updateRequest,
                                 RedirectAttributes redirectAttributes) {

        final String email = SecurityUtils.getUsername();

        if (!userService.changePassword(email, updateRequest)) {
            redirectAttributes.addAttribute("passwordError", true);
            return "redirect:edit";
        }
        redirectAttributes.addAttribute("passwordChange", true);
        return "redirect:../profile";
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public String authExceptionHandler(RuntimeException exception,
                                       RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("passwordMismatch", exception.getMessage());
        return "redirect:edit";
    }
}
