/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.models;

import io.swagger.annotations.ApiModelProperty;
import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Common attributes for all entities
 */
public abstract class BaseEntity {

    @ApiModelProperty(hidden = true)
    private UUID id;

    @ApiModelProperty(hidden = true)
    private DateTime createdOn;

    @ApiModelProperty(hidden = true)
    private String createdBy;

    @ApiModelProperty(hidden = true)
    private DateTime modifiedOn;

    @ApiModelProperty(hidden = true)
    private String modifiedBy;

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(DateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public DateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(DateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
