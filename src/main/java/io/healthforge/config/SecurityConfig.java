/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.config;

import io.healthforge.models.security.User;
import io.healthforge.security.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Security configuration.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityContext securityContext() {
        SecurityContext securityContext = new SecurityContext();
        securityContext.setCurrentUser(new User("testuser", "Test User"));
        return securityContext;
    }
}
