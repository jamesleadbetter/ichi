/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.rest;

import io.healthforge.models.GenericResponse;
import io.healthforge.models.ResultSet;
import io.healthforge.models.orders.Order;
import io.healthforge.services.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jodd.util.MimeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * CRUD REST API for handling orders
 */
@RestController
@Api(value = "order", description = "Order API")
@RequestMapping("/api/order")
public class OrderApi {

    private final static Logger logger = LoggerFactory.getLogger(OrderApi.class);

    @Autowired
    private OrderService service;

    @RequestMapping(value = "/heartbeat", method = RequestMethod.GET,
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = { "order" }, value = "Call to check the service is alive", notes = "Returns a simple response if alive.")
    public GenericResponse heartbeat() {
        return GenericResponse.OK();
    }

    @RequestMapping(value = "", method = RequestMethod.GET,
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = { "order" }, value = "Gets an order list.")
    public ResultSet<Order> getAll(
            @RequestParam(name="offset", defaultValue="0", required=false) Integer offset,
            @RequestParam(name="limit", defaultValue="10", required=false) Integer limit) {
        Map<String, Object> searchParams = new HashMap<>();
        return service.get(offset, limit, searchParams);
    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET,
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = { "order" }, value = "Get by id.", notes = "Returns an order by id.")
    public Order get(@PathVariable String orderId) throws NotFoundException {
        return service.get(UUID.fromString(orderId));
    }

    @RequestMapping(value = "", method = RequestMethod.POST,
            consumes = {MimeTypes.MIME_APPLICATION_JSON},
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = { "order" }, value = "Creates an order.")
    public Order post(@RequestBody Order entity) throws NotFoundException {
        return service.add(entity);
    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.PUT,
            consumes = {MimeTypes.MIME_APPLICATION_JSON},
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = { "order" }, value = "Updates an order.",
            notes = "Updates an order.")
    public Order put(@PathVariable String orderId, @RequestBody Order entity) throws NotFoundException {
        if(!entity.getId().equals(UUID.fromString(orderId))) {
            throw new NotFoundException();
        }
        return service.update(entity);
    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.DELETE,
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = { "order" }, value = "Deletes an order.", notes = "")
    public GenericResponse delete(@PathVariable String orderId)throws NotFoundException {
        service.remove(UUID.fromString(orderId));
        return GenericResponse.OK();
    }
}
