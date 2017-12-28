package io.healthforge.models;

import io.healthforge.models.component.AppointmentStatus;
import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Models an appointment for a patient to see a clinician
 */
public class Appointment extends BaseEntity {

    private AppointmentStatus status;
    private DateTime startsAt;
    private DateTime endAt;
    private UUID patientId;
    private UUID clinicianId;
    private String reason;

    /**
     * @return the current status of the appointment
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * Sets the current status of the appointment
     *
     * @param status the status
     */
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    /**
     * @return the start date and time of the appointment
     */
    public DateTime getStartsAt() {
        return startsAt;
    }

    /**
     * Sets the start date and time of the appointment
     *
     * @param startsAt the start date and time
     */
    public void setStartsAt(DateTime startsAt) {
        this.startsAt = startsAt;
    }

    /**
     * @return the end date and time of the appointment
     */
    public DateTime getEndAt() {
        return endAt;
    }

    /**
     * Sets the end date and time of the appointment
     *
     * @param endAt the end date and time
     */
    public void setEndAt(DateTime endAt) {
        this.endAt = endAt;
    }

    /**
     * @return the ID of the patient
     */
    public UUID getPatientId() {
        return patientId;
    }

    /**
     * Sets the ID of the patient
     *
     * @param patientId the ID
     */
    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    /**
     * @return the ID of the clinician
     */
    public UUID getClinicianId() {
        return clinicianId;
    }

    /**
     * Sets the ID of the clinician
     *
     * @param clinicianId the ID
     */
    public void setClinicianId(UUID clinicianId) {
        this.clinicianId = clinicianId;
    }

    /**
     * @return the reason for the appointment
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason for the appointment
     *
     * @param reason the reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

}
