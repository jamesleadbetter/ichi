/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.models;

import java.util.List;

/**
 * Represents a physical or postal address.
 */
public class Address {

    private String type;
    private List<String> lines;
    private String country;

    public Address() {
    }

    public Address(String type) {
        this.type = type;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
