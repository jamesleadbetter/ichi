package io.healthforge.services.impl;

import io.healthforge.exception.InvalidModelException;
import io.healthforge.exception.NotFoundException;
import io.healthforge.models.Appointment;
import io.healthforge.models.component.AppointmentStatus;
import io.healthforge.services.AppointmentService;
import io.healthforge.validation.AppointmentValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link AppointmentService} backed by a {@link java.util.concurrent.CopyOnWriteArrayList}
 */
@Component
public class AppointmentServiceImpl extends BaseServiceImpl<Appointment> implements AppointmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    private final AppointmentValidationService appointmentValidationService;

    /**
     * Constructs a new {@link AppointmentServiceImpl} instance
     *
     * @param appointmentValidationService the service to use for validating appointments
     */
    public AppointmentServiceImpl(final AppointmentValidationService appointmentValidationService) {
        this.appointmentValidationService = appointmentValidationService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Appointment add(final Appointment item) {
        // created appointments always in the new status
        item.setStatus(AppointmentStatus.NEW);
        return super.add(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Appointment update(Appointment appointment) throws NotFoundException, InvalidModelException {
        final Appointment oldAppointment = this.get(appointment.getId());

        if (oldAppointment == null) {
            final String errorMessage = MessageFormat.format("Error updating appointment - could not find " +
                    "appointment" +

                    " with ID {0}", appointment.getId());
            LOGGER.debug(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        if (!this.appointmentValidationService.isStatusTransitionValid(oldAppointment.getStatus(), appointment
                .getStatus())) {
            final String errorMessage = MessageFormat.format("Error updating appointment with ID {0} - invalid " +
                    "status" +
                    " transition from {1} to {2}", appointment.getId(), oldAppointment.getStatus(), appointment
                    .getStatus());
            LOGGER.debug(errorMessage);
            throw new InvalidModelException(errorMessage);
        }

        return super.update(appointment);
    }

    /**
     * Filters the provided list of {@link Appointment} based upon the provided search parameters
     *
     * @param items        the list to be filtered
     * @param searchParams the filtering criteria
     * @return the filtered list
     */
    @Override
    protected List<Appointment> getDoFilter(final List<Appointment> items, final Map<String, Object> searchParams) {
        if (searchParams.containsKey(AppointmentService.PATIENT_ID_PARAM)) {
            final UUID patientId = UUID.fromString(String.class.cast(searchParams.get(PATIENT_ID_PARAM)));
            return items.stream().filter(app -> patientId.equals(app.getPatientId())).collect(Collectors.toList());
        }

        return items;
    }
}
