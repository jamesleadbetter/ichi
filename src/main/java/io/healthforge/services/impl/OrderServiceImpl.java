/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.services.impl;

import io.healthforge.events.Dispatcher;
import io.healthforge.events.handlers.orders.NewOrderEvent;
import io.healthforge.events.handlers.orders.UpdateOrderEvent;
import io.healthforge.models.orders.Order;
import io.healthforge.rest.NotFoundException;
import io.healthforge.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Order service implementation
 */
@Component
public class OrderServiceImpl extends BaseServiceImpl<Order> implements OrderService {

    @Autowired
    private Dispatcher dispatcher;

    @Override
    protected List<Order> getDoFilter(List<Order> items, Map<String, Object> searchParams) {
        return items;
    }

    @Override
    public Order add(Order item) {
        item = super.add(item);
        dispatcher.dispatch(new NewOrderEvent(this, item));
        return item;
    }

    @Override
    public Order update(Order item) throws NotFoundException {
        item = super.update(item);
        dispatcher.dispatch(new UpdateOrderEvent(this, item));
        return item;
    }

    @Override
    public void remove(UUID id) throws NotFoundException {
        Order item = get(id);
        super.remove(id);
        dispatcher.dispatch(new UpdateOrderEvent(this, item));
    }

    @Override
    public void remove(Order item) {
        super.remove(item);
        dispatcher.dispatch(new UpdateOrderEvent(this, item));
    }
}
