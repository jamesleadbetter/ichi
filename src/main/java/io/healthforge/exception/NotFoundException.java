/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.exception;

/**
 * Exception to be thrown when an entity cannot be found
 */
public class NotFoundException extends Exception {

    /**
     * Constructs a new {@link NotFoundException}
     */
    public NotFoundException() {
    }

    /**
     * Constructs a new {@link NotFoundException}
     *
     * @param message the error message
     */
    public NotFoundException(final String message) {
        super(message);
    }
}