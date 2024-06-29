package com.yelzhan.capstoneproject.controller;

import com.yelzhan.capstoneproject.exception.PasswordMismatchException;
import com.yelzhan.capstoneproject.exception.ResourceAlreadyExistException;
import com.yelzhan.capstoneproject.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorControllerTest {

    @Test
    public void testErrorPage() {
        ErrorController adminController = new ErrorController();
        HttpServletRequest request = new MockHttpServletRequest();
        Model model = new ConcurrentModel();

        String viewName = adminController.errorPage(request, model);

        assertEquals("error", viewName);
        assertEquals("Error page", model.getAttribute("pageTitle"));
        assertEquals(request.getHeader("Referer"), model.getAttribute("previousPage"));
    }

    @Test
    public void testResourceNotFoundExceptionHandler() {
        ErrorController adminController = new ErrorController();
        RuntimeException exception = new ResourceNotFoundException("Resource not found");
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String viewName = adminController.resourceNotFoundExceptionHandler(exception, redirectAttributes);

        assertEquals("redirect:/error", viewName);
        assertEquals("Resource not found", redirectAttributes.get("errorMessage"));
    }

    @Test
    public void testResourceAlreadyExistExceptionHandler() {
        ErrorController adminController = new ErrorController();
        RuntimeException exception = new ResourceAlreadyExistException("Resource already exists");
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String viewName = adminController.resourceAlreadyExistExceptionHandler(exception, redirectAttributes);

        assertEquals("redirect:/error", viewName);
        assertEquals("Resource already exists", redirectAttributes.get("errorMessage"));
    }

    @Test
    public void testPasswordMismatchExceptionHandler() {
        ErrorController adminController = new ErrorController();
        RuntimeException exception = new PasswordMismatchException("Password mismatch");
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String viewName = adminController.passwordMismatchExceptionHandler(exception, redirectAttributes);

        assertEquals("redirect:/error", viewName);
        assertEquals("Password mismatch", redirectAttributes.get("errorMessage"));
    }
}
