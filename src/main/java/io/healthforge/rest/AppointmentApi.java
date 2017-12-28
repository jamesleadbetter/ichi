package io.healthforge.rest;

import io.healthforge.exception.InvalidModelException;
import io.healthforge.exception.NotFoundException;
import io.healthforge.models.Appointment;
import io.healthforge.models.GenericResponse;
import io.healthforge.models.ResultSet;
import io.healthforge.services.AppointmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jodd.util.MimeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * CRUD REST API for handling appointments
 */
@RestController
@Api(value = "appointment", description = "Appointment API")
@RequestMapping("/api/appointments")
public class AppointmentApi {

    private final static Logger LOGGER = LoggerFactory.getLogger(AppointmentApi.class);

    private final AppointmentService appointmentService;

    /**
     * Constructs a new {@link AppointmentApi} instance
     *
     * @param appointmentService the service to use for reading and writing appointments
     */
    @Autowired
    public AppointmentApi(final AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * REST endpoint for listing all appointments with paging
     *
     * @param offset    the cursor position
     * @param limit     the number to return
     * @param patientId optional query parameter to filter appointments by patient ID
     * @return a list of appointments
     */
    @RequestMapping(value = "", method = RequestMethod.GET,
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = {"appointment"}, value = "Gets an appointment list.")
    public ResultSet<Appointment> getAll(
            @RequestParam(name = "offset", defaultValue = "0", required = false) final Integer offset,
            @RequestParam(name = "limit", defaultValue = "10", required = false) final Integer limit,
            @RequestParam(name = "patientId", required = false) final String patientId) {
        final Map<String, Object> searchParams = new HashMap<>();

        if (StringUtils.hasText(patientId)) {
            searchParams.put(AppointmentService.PATIENT_ID_PARAM, patientId);
        }

        return this.appointmentService.get(offset, limit, searchParams);
    }

    /**
     * REST endpoint for retrieving an appointment by its ID
     *
     * @param appointmentId the ID of the appointment
     * @return the appointment
     * @throws NotFoundException when the appointment with the requested ID cannot be found
     */
    @RequestMapping(value = "/{appointmentId}", method = RequestMethod.GET,
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = {"appointment"}, value = "Get by id.", notes = "Returns an appointment by id.")
    public Appointment get(@PathVariable final String appointmentId) throws NotFoundException {
        return this.appointmentService.get(UUID.fromString(appointmentId));
    }

    /**
     * REST endpoint for creating an appointment
     *
     * @param appointment the appointment to be created
     * @return the created appointment
     */
    @RequestMapping(value = "", method = RequestMethod.POST,
            consumes = {MimeTypes.MIME_APPLICATION_JSON},
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = {"appointment"}, value = "Creates an appointment.")
    public Appointment create(@RequestBody final Appointment appointment) {
        return this.appointmentService.add(appointment);
    }

    /**
     * REST endpoint for updating an appointment
     *
     * @param appointmentId the ID of the appointment to update
     * @param appointment   the appointment to update
     * @return the updated appointment
     * @throws NotFoundException     when the appointment to be updated cannot be found
     * @throws InvalidModelException when the appointment to be updated is not valid
     */
    @RequestMapping(value = "/{appointmentId}", method = RequestMethod.PUT,
            consumes = {MimeTypes.MIME_APPLICATION_JSON},
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = {"appointment"}, value = "Updates an appointment.",
            notes = "Updates an appointment.")
    public Appointment update(@PathVariable final String appointmentId, @RequestBody final Appointment appointment)
            throws
            NotFoundException, InvalidModelException {

        final UUID id = UUID.fromString(appointmentId);

        if (!appointment.getId().equals(id)) {
            final String errorMessage = MessageFormat.format("Appointment ID in path: {0} does not match ID in body " +
                    "{1}", id, appointment.getId());
            LOGGER.debug(errorMessage);
            throw new InvalidModelException(errorMessage);
        }

        return this.appointmentService.update(appointment);
    }

    /**
     * REST endpoint for deleting an appointment
     *
     * @param appointmentId the ID of the appointment to delete
     * @return an HTTP response
     * @throws NotFoundException when the appointment to be deleted cannot be found
     */
    @RequestMapping(value = "/{appointmentId}", method = RequestMethod.DELETE,
            produces = {MimeTypes.MIME_APPLICATION_JSON})
    @ApiOperation(tags = {"appointment"}, value = "Deletes a appointment.", notes = "")
    public GenericResponse delete(@PathVariable final String appointmentId) throws NotFoundException {
        this.appointmentService.remove(UUID.fromString(appointmentId));
        return GenericResponse.OK();
    }


}