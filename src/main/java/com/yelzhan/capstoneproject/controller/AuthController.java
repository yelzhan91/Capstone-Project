package com.yelzhan.capstoneproject.controller;

import com.yelzhan.capstoneproject.config.security.SecurityUtils;
import com.yelzhan.capstoneproject.exception.PasswordMismatchException;
import com.yelzhan.capstoneproject.exception.ResourceAlreadyExistException;
import com.yelzhan.capstoneproject.exception.ResourceNotFoundException;
import com.yelzhan.capstoneproject.model.dto.request.auth.LoginRequest;
import com.yelzhan.capstoneproject.model.dto.request.auth.RegistrationRequest;
import com.yelzhan.capstoneproject.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.yelzhan.capstoneproject.config.ApplicationConstants.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/signup")
    public String signupForm(Model model) {

        Authentication authentication = SecurityUtils.getAuthentication();

        if (authentication == null || SecurityUtils.isAnonymousAuthenticationToken(authentication)) {
            model.addAttribute(PAGE_TITLE.getValue(), SIGNUP.getValue());
            model.addAttribute("request", new RegistrationRequest());
            return "signup";
        }
        return "redirect:/";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("request") @Valid RegistrationRequest registrationRequest,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("failed", true);
            bindingResult.getFieldErrors()
                    .forEach(fieldError -> redirectAttributes.addAttribute(fieldError.getField(),
                            fieldError.getDefaultMessage()));
            return "redirect:signup";
        }
        authService.register(registrationRequest);
        return "redirect:login?registration=success";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {

        Authentication authentication = SecurityUtils.getAuthentication();

        if (authentication == null || SecurityUtils.isAnonymousAuthenticationToken(authentication)) {
            model.addAttribute(PAGE_TITLE.getValue(), LOGIN.getValue());
            model.addAttribute("request", new LoginRequest());
            return "login";
        }
        return "redirect:/";
    }

}
