/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.models.security;

import java.util.List;

/**
 * Represents a group
 */
public class Group {

    private String id;

    private String name;

    private List<User> groupMembers;

    public Group() {
    }

    public Group(String id, String name, List<User> groupMembers) {
        this.id = id;
        this.name = name;
        this.groupMembers = groupMembers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<User> groupMembers) {
        this.groupMembers = groupMembers;
    }
}
