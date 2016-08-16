/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.events.handlers.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.healthforge.models.orders.OrderStatus;
import io.healthforge.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

/**
 * Handle events for orders.
 */
@Component
public class OrdersEventHandler {

    private final static Logger logger = LoggerFactory.getLogger(OrdersEventHandler.class);

    @Autowired
    Jackson2ObjectMapperBuilder builder;

    @Autowired
    private OrderService orderService;

    @EventListener
    public void onNewOrderEvent(NewOrderEvent newOrderEvent) throws JsonProcessingException {
        logger.info("Order Created: {} {} {}", newOrderEvent.getOrder().getOrderStatus(), newOrderEvent.getOrder().getPatientId(), newOrderEvent.getOrder().getClinicianId());
        if(logger.isTraceEnabled()) {
            logger.trace("{}", builder.build().writeValueAsString(newOrderEvent.getOrder()));
        }
    }

    @EventListener
    public void onUpdateOrderEvent(UpdateOrderEvent updateOrderEvent) throws JsonProcessingException {
        ObjectMapper objectMapper = builder.build();
        logger.info("Order Updated: {} {} {}", updateOrderEvent.getOrder().getOrderStatus(), updateOrderEvent.getOrder().getPatientId(), updateOrderEvent.getOrder().getClinicianId());
        if(logger.isTraceEnabled()) {
            logger.trace("{}", builder.build().writeValueAsString(updateOrderEvent.getOrder()));
        }

        if(updateOrderEvent.getOrder().getOrderStatus().equals(OrderStatus.COMPLETED) ||
                updateOrderEvent.getOrder().getOrderStatus().equals(OrderStatus.ABORTED)) {
            //
            // Process order
            //
        }
    }
}
