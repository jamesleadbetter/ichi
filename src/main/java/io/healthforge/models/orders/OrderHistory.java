/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.models.orders;

import io.healthforge.models.security.User;
import org.joda.time.DateTime;

public class OrderHistory {

    private DateTime timestamp;
    private User user;
    private String narrative;

    public OrderHistory() {
    }

    public OrderHistory(DateTime timestamp, User user, String narrative) {
        this.timestamp = timestamp;
        this.user = user;
        this.narrative = narrative;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }
}
