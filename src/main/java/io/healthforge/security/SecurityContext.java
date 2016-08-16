/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.security;

import io.healthforge.models.security.User;

/**
 * Security context, holding the current user's details.
 */
public class SecurityContext {

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
