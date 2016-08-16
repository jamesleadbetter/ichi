/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.models;

import org.joda.time.DateTime;

import java.util.List;

public class Patient extends BaseEntity {

    private List<String> identifiers;
    private String firstName;
    private String lastName;
    private DateTime dateOfBirth;
    private DateTime dateOfDeath;
    private List<Address> addresses;

    public Patient() {
    }

    public Patient(List<String> identifiers, String firstName, String lastName, DateTime dateOfBirth) {
        this.identifiers = identifiers;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public Patient(List<String> identifiers, String firstName, String lastName, DateTime dateOfBirth, List<Address> addresses) {
        this.identifiers = identifiers;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.addresses = addresses;
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

    public DateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(DateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public DateTime getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(DateTime dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}
