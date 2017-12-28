package io.healthforge.validation;

import io.healthforge.models.component.AppointmentStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Component for validating Appointments
 */
@Component
public class AppointmentValidationService {

    private final Map<AppointmentStatus, List<AppointmentStatus>> validTransitions;

    /**
     * Constructs a new {@link AppointmentValidationService} instance
     */
    public AppointmentValidationService() {
        this.validTransitions = this.buildValidStatusTransitions();
    }

    /**
     * Determines whether the status transition of an appointment is valid
     *
     * @param oldAppointmentStatus the status the appointment is transitioning from
     * @param newAppointmentStatus the status the appointment is transitioning to
     * @return whether the transition is valid
     */
    public boolean isStatusTransitionValid(final AppointmentStatus oldAppointmentStatus, final AppointmentStatus
            newAppointmentStatus) {
        if (oldAppointmentStatus == null || newAppointmentStatus == null) {
            return false;
        }

        if (oldAppointmentStatus.equals(newAppointmentStatus)) {
            return true;
        }

        final List<AppointmentStatus> validNewStates = this.validTransitions.getOrDefault(oldAppointmentStatus, new
                ArrayList<>());

        return validNewStates.contains(newAppointmentStatus);
    }

    /**
     * Constructs a map of {@link AppointmentStatus} and their corresponding statuses they may transition to
     *
     * @return the valid transition statuses
     */
    private Map<AppointmentStatus, List<AppointmentStatus>> buildValidStatusTransitions() {
        final Map<AppointmentStatus, List<AppointmentStatus>> validTransitions = new HashMap<>();
        validTransitions.put(AppointmentStatus.NEW, Arrays.asList(AppointmentStatus.PENDING,
                AppointmentStatus.CANCEL));
        validTransitions.put(AppointmentStatus.PENDING, Arrays.asList(AppointmentStatus.IN_PROGRESS,
                AppointmentStatus.CANCEL, AppointmentStatus.DNA));
        validTransitions.put(AppointmentStatus.IN_PROGRESS, Collections.singletonList(AppointmentStatus.COMPLETE));
        validTransitions.put(AppointmentStatus.COMPLETE, new ArrayList<>());
        validTransitions.put(AppointmentStatus.CANCEL, Collections.singletonList(AppointmentStatus.CANCELLED));
        validTransitions.put(AppointmentStatus.CANCELLED, new ArrayList<>());
        validTransitions.put(AppointmentStatus.DNA, new ArrayList<>());

        return validTransitions;
    }
}
