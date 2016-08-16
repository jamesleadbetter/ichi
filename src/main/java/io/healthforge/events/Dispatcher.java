/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Dispatches events to other components.
 *
 * Uses the Spring application event framework.
 */
@Component
public class Dispatcher {

    @Autowired
    private ApplicationEventPublisher publisher;

    public void dispatch(ApplicationEvent event) {
        publisher.publishEvent(event);
    }
}
