package com.luv2code.WebScraperDB1.service;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SecurityServiceImpl {
    public boolean isSecurityEnabledForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            // Check if the user has ADMIN role
            return authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        }
        // Security is enabled by default
        return true;
    }
}
