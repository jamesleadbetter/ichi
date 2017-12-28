/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.rest;

import io.healthforge.exception.InvalidModelException;
import io.healthforge.exception.NotFoundException;
import io.healthforge.models.GenericResponse;
import io.healthforge.models.Patient;
import io.healthforge.models.ResultSet;
import io.healthforge.services.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jodd.util.MimeTypes;
import jodd.util.URLDecoder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * CRUD REST API for handling patients
 */
@RestController
@Api(value = "patient", description = "Patient API")
@RequestMapping("/api/patient")
public class PatientApi {

    private final static Logger logger = LoggerFactory.getLogger(PatientApi.class);

    @Autowired
    private PatientService service;

    @RequestMapping(value = "/heartbeat", method = RequestMethod.GET,
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = { "patient" }, value = "Call to check the service is alive", notes = "Returns a simple response if alive.")
    public GenericResponse heartbeat() {
        return GenericResponse.OK();
    }

    @RequestMapping(value = "", method = RequestMethod.GET,
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = { "patient" }, value = "Gets a patient list.")
    public ResultSet<Patient> getAll(
            @RequestParam(name="offset", defaultValue="0", required=false) Integer offset,
            @RequestParam(name="limit", defaultValue="10", required=false) Integer limit,
            @ApiParam(example = "10 Jan 1980")
            @RequestParam(name="dob", required=false) String dob,
            @RequestParam(name = "postcode", required=false) final String postcode) {

        Map<String, Object> searchParams = new HashMap<>();
        if(StringUtils.hasText(dob)) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMM yyyy");
            searchParams.put("dob", DateTime.parse(URLDecoder.decode(dob), formatter));
        }

        if (StringUtils.hasText(postcode)) {
            searchParams.put(PatientService.POSTCODE_PARAM, postcode);
        }

        return service.get(offset, limit, searchParams);
    }

    @RequestMapping(value = "/{patientId}", method = RequestMethod.GET,
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = { "patient" }, value = "Get by id.", notes = "Returns a patient by id.")
    public Patient get(@PathVariable String patientId) throws NotFoundException {
        return service.get(UUID.fromString(patientId));
    }

    @RequestMapping(value = "", method = RequestMethod.POST,
            consumes = {MimeTypes.MIME_APPLICATION_JSON},
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = { "patient" }, value = "Creates a patient.")
    public Patient post(@RequestBody Patient entity) throws NotFoundException {
        return service.add(entity);
    }

    @RequestMapping(value = "/{patientId}", method = RequestMethod.PUT,
            consumes = {MimeTypes.MIME_APPLICATION_JSON},
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = { "patient" }, value = "Updates a patient.",
            notes = "Updates a patient.")
    public Patient put(@PathVariable String patientId, @RequestBody Patient entity) throws NotFoundException, InvalidModelException {
        if(!entity.getId().equals(UUID.fromString(patientId))) {
            throw new NotFoundException();
        }
        return service.update(entity);
    }

    @RequestMapping(value = "/{patientId}", method = RequestMethod.DELETE,
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = { "patient" }, value = "Deletes a patient.", notes = "")
    public GenericResponse delete(@PathVariable String patientId)throws NotFoundException {
        service.remove(UUID.fromString(patientId));
        return GenericResponse.OK();
    }
}
