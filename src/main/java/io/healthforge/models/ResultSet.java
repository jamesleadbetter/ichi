/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.models;

import java.util.List;

/**
 * Represents a result set from a search operation
 *
 * @param <T> Entity type
 */
public class ResultSet<T> {

    private int totalItems;
    private List<T> items;

    public ResultSet(int totalItems, List<T> items) {

        this.totalItems = totalItems;
        this.items = items;
    }

    public ResultSet() {
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }


}
