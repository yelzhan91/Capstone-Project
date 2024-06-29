package com.yelzhan.capstoneproject.controller;

import com.yelzhan.capstoneproject.exception.PasswordMismatchException;
import com.yelzhan.capstoneproject.exception.ResourceAlreadyExistException;
import com.yelzhan.capstoneproject.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.yelzhan.capstoneproject.config.ApplicationConstants.PAGE_TITLE;

@Controller
@ControllerAdvice
@RequestMapping("/error")
public class ErrorController {

    private static final String REFERER = "Referer";

    @GetMapping
    public String errorPage(HttpServletRequest request,  Model model){
        model.addAttribute(PAGE_TITLE.getValue(), "Error page");
        model.addAttribute("previousPage", request.getHeader(REFERER));
        return "error";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String resourceNotFoundExceptionHandler(RuntimeException exception, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("errorMessage", exception.getMessage());
        return "redirect:/error";
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public String resourceAlreadyExistExceptionHandler(RuntimeException exception, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("errorMessage", exception.getMessage());
        return "redirect:/error";
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public String passwordMismatchExceptionHandler(RuntimeException exception, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("errorMessage", exception.getMessage());
        return "redirect:/error";
    }

}
