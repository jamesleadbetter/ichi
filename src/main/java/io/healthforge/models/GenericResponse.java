/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.models;

/**
 * Standard simple response type with status code and message
 */
public class GenericResponse {

    private GenericResponseCode responseCode;
    private String errorMessage;

    public GenericResponse(GenericResponseCode responseCode, String errorMessage) {
        this.responseCode = responseCode;
        this.errorMessage = errorMessage;
    }

    public GenericResponse() {

    }

    public GenericResponse(GenericResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public static GenericResponse OK() {
        return new GenericResponse(GenericResponseCode.OK);
    }

    public static GenericResponse ERROR() {
        return new GenericResponse(GenericResponseCode.ERROR);
    }

    public static GenericResponse SECURITY() {
        return new GenericResponse(GenericResponseCode.SECURITY);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public GenericResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(GenericResponseCode responseCode) {
        this.responseCode = responseCode;
    }
}
