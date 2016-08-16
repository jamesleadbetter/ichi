/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.services;

import io.healthforge.models.BaseEntity;
import io.healthforge.models.ResultSet;
import io.healthforge.rest.NotFoundException;

import java.util.Map;
import java.util.UUID;

/**
 * A generic CRUD service.
 *
 * @param <T> The entity type.
 */
public interface BaseService<T extends BaseEntity> {

    T add(T item);

    T get(UUID id) throws NotFoundException;

    ResultSet<T> get(int offset, int limit, Map<String, Object> searchParams);

    T update(T item) throws NotFoundException;

    void remove(T item);

    void remove(UUID id) throws NotFoundException;

    int size();
}
