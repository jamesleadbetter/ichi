/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.services.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.healthforge.exception.InvalidModelException;
import io.healthforge.models.BaseEntity;
import io.healthforge.models.ResultSet;
import io.healthforge.exception.NotFoundException;
import io.healthforge.security.SecurityContext;
import io.healthforge.services.BaseService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Supertype service implementation
 */
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    private final static Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

    protected CopyOnWriteArrayList<T> items;

    public BaseServiceImpl() {
        this.items = new CopyOnWriteArrayList<T>();
    }

    @Autowired
    private SecurityContext securityContext;

    @Override
    public T add(T item) {
        item.setId(UUID.randomUUID());
        item.setCreatedOn(new DateTime());
        item.setCreatedBy(securityContext.getCurrentUser().getUsername());
        this.items.add(item);
        return item;
    }

    @Override
    public T get(UUID id) throws NotFoundException {

        T item = Iterables.find(this.items, new Predicate<T>() {
            @Override
            public boolean apply(T t) {
                return t.getId().equals(id);
            }
        }, null);

        if(item == null) {
            throw new NotFoundException();
        }

        return item;
    }

    @Override
    public ResultSet<T> get(int offset, int limit, Map<String, Object> searchParams) {
        List<T> items = null;
        int lastIndex = offset + limit;
        if(lastIndex > this.items.size()) {
            lastIndex = this.items.size();
        }
        if(this.items.size() == 0 || offset > this.items.size() || lastIndex < offset) {
            items = Lists.newArrayList();
        }
        items = this.items.subList(offset, lastIndex);
        if(searchParams != null) {
            items = getDoFilter(items, searchParams);
        }
        return new ResultSet<T>(this.items.size(), items);
    }

    protected abstract List<T> getDoFilter(List<T> items, Map<String, Object> searchParams);

    @Override
    public T update(T item) throws NotFoundException, InvalidModelException {
        item.setModifiedOn(new DateTime());
        item.setModifiedBy(securityContext.getCurrentUser().getUsername());
        T oldItem = get(item.getId());
        int index = items.indexOf(oldItem);
        this.items.set(index, item);
        return item;
    }

    @Override
    public void remove(T item) {
        this.items.remove(item);
    }

    @Override
    public void remove(UUID id) throws NotFoundException {
        this.items.remove(get(id));
    }

    public int size() {
        return this.items.size();
    }
}
