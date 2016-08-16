/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.models.orders;

import io.healthforge.models.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Basic order history.
 */
public abstract class Order extends BaseEntity {

    private OrderStatus orderStatus;
    private UUID patientId;
    private UUID clinicianId;
    private List<OrderHistory> orderHistory;

    public Order() {
    }

    public Order(OrderStatus orderStatus, UUID patientId, UUID clinicianId) {
        this.orderStatus = orderStatus;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.orderHistory = new ArrayList<>();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public UUID getClinicianId() {
        return clinicianId;
    }

    public void setClinicianId(UUID clinicianId) {
        this.clinicianId = clinicianId;
    }

    public List<OrderHistory> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<OrderHistory> orderHistory) {
        this.orderHistory = orderHistory;
    }
}
