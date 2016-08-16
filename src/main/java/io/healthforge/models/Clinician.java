/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.models;

import java.util.List;

/**
 * A model representing a clinician or healthcare professional
 */
public class Clinician extends BaseEntity {

    private List<String> identifiers;
    private String firstName;
    private String lastName;
    private List<Address> addresses;

    public Clinician() {
    }

    public Clinician(List<String> identifiers, String firstName, String lastName) {
        this.identifiers = identifiers;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<String> identifiers) {
        this.identifiers = identifiers;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
