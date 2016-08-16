/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.models.orders;

import java.util.UUID;

/**
 * A simple text based order.
 */
public class SimpleOrder extends Order {

    private String orderText;

    public SimpleOrder() {
    }

    public SimpleOrder(OrderStatus orderStatus, UUID patientId, UUID clinicianId, String orderText) {
        super(orderStatus, patientId, clinicianId);
        this.orderText = orderText;
    }

    public String getOrderText() {
        return orderText;
    }

    public void setOrderText(String orderText) {
        this.orderText = orderText;
    }
}
