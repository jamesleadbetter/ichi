package io.healthforge.services;

import io.healthforge.models.Appointment;

/**
 * Interface for a CRUD Service for managing {@link Appointment}
 */
public interface AppointmentService extends BaseService<Appointment> {

    String PATIENT_ID_PARAM = "patientId";
}
