package com.yelzhan.capstoneproject.config.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getUsername() {
        return getAuthentication().getName();
    }

    public static boolean isAnonymousAuthenticationToken(Authentication authentication) {
        return authentication instanceof AnonymousAuthenticationToken;
    }

}
