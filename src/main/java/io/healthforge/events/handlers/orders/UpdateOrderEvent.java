/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.events.handlers.orders;

import io.healthforge.models.orders.Order;
import org.springframework.context.ApplicationEvent;

/**
 * This event is fired when an order is updated.
 */
public class UpdateOrderEvent extends ApplicationEvent {

    private Order order;

    public UpdateOrderEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
