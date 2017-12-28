/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.rest;

import io.healthforge.exception.InvalidModelException;
import io.healthforge.exception.NotFoundException;
import io.healthforge.models.Clinician;
import io.healthforge.models.GenericResponse;
import io.healthforge.models.ResultSet;
import io.healthforge.services.ClinicianService;
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
 * CRUD REST API for handling clinicians
 */
@RestController
@Api(value = "clinician", description = "Clinician API")
@RequestMapping("/api/clinician")
public class ClinicianApi {

    private final static Logger logger = LoggerFactory.getLogger(ClinicianApi.class);

    @Autowired
    private ClinicianService service;

    @RequestMapping(value = "/heartbeat", method = RequestMethod.GET,
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = { "clinician" }, value = "Call to check the service is alive", notes = "Returns a simple response if alive.")
    public GenericResponse heartbeat() {
        return GenericResponse.OK();
    }

    @RequestMapping(value = "", method = RequestMethod.GET,
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = { "clinician" }, value = "Gets a clinician list.")
    public ResultSet<Clinician> getAll(
            @RequestParam(name="offset", defaultValue="0", required=false) Integer offset,
            @RequestParam(name="limit", defaultValue="10", required=false) Integer limit) {
        Map<String, Object> searchParams = new HashMap<>();
        return service.get(offset, limit, searchParams);
    }

    @RequestMapping(value = "/{clinicianId}", method = RequestMethod.GET,
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = { "clinician" }, value = "Get by id.", notes = "Returns a clinician by id.")
    public Clinician get(@PathVariable String clinicianId) throws NotFoundException {
        return service.get(UUID.fromString(clinicianId));
    }

    @RequestMapping(value = "", method = RequestMethod.POST,
            consumes = {MimeTypes.MIME_APPLICATION_JSON},
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = { "clinician" }, value = "Creates a clinician.")
    public Clinician post(@RequestBody Clinician entity) throws NotFoundException {
        return service.add(entity);
    }

    @RequestMapping(value = "/{clinicianId}", method = RequestMethod.PUT,
            consumes = {MimeTypes.MIME_APPLICATION_JSON},
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = { "clinician" }, value = "Updates a clinician.",
            notes = "Updates a clinician.")
    public Clinician put(@PathVariable String clinicianId, @RequestBody Clinician entity) throws NotFoundException, InvalidModelException {
        if(!entity.getId().equals(UUID.fromString(clinicianId))) {
            throw new NotFoundException();
        }
        return service.update(entity);
    }

    @RequestMapping(value = "/{clinicianId}", method = RequestMethod.DELETE,
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = { "clinician" }, value = "Deletes a clinician.", notes = "")
    public GenericResponse delete(@PathVariable String clinicianId)throws NotFoundException {
        service.remove(UUID.fromString(clinicianId));
        return GenericResponse.OK();
    }
}
