/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Returns HTTP 404
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends Exception {
}