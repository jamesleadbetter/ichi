package io.healthforge.validation;

import io.healthforge.models.component.AppointmentStatus;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link AppointmentValidationService}
 */
public class AppointmentValidationServiceTest {

    private final AppointmentValidationService testClass = new AppointmentValidationService();

    /**
     * Tests the isStatusTransitionValid method for null statuses
     */
    @Test
    public void testStatusTransitionValidationForNullStatuses() {
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(null, null));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.NEW,
                null));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(null, AppointmentStatus
                .PENDING));
    }

    /**
     * Tests the isStatusTransitionValid method from the NEW status
     */
    @Test
    public void testStatusTransitionValidationFromNewStatus() {
        assertTrue("Transition should be valid", this.testClass.isStatusTransitionValid(AppointmentStatus.NEW,
                AppointmentStatus.NEW));
        assertTrue("Transition should be valid", this.testClass.isStatusTransitionValid(AppointmentStatus.NEW,
                AppointmentStatus.PENDING));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.NEW,
                AppointmentStatus.IN_PROGRESS));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.NEW,
                AppointmentStatus.COMPLETE));
        assertTrue("Transition should be valid", this.testClass.isStatusTransitionValid(AppointmentStatus.NEW,
                AppointmentStatus.CANCEL));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.NEW,
                AppointmentStatus.CANCELLED));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.NEW,
                AppointmentStatus.DNA));
    }

    /**
     * Tests the isStatusTransitionValid method from the PENDING status
     */
    @Test
    public void testStatusTransitionValidationFromPendingStatus() {
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.PENDING,
                AppointmentStatus.NEW));
        assertTrue("Transition should be valid", this.testClass.isStatusTransitionValid(AppointmentStatus.PENDING,
                AppointmentStatus.PENDING));
        assertTrue("Transition should be valid", this.testClass.isStatusTransitionValid(AppointmentStatus.PENDING,
                AppointmentStatus.IN_PROGRESS));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.PENDING,
                AppointmentStatus.COMPLETE));
        assertTrue("Transition should be valid", this.testClass.isStatusTransitionValid(AppointmentStatus.PENDING,
                AppointmentStatus.CANCEL));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.PENDING,
                AppointmentStatus.CANCELLED));
        assertTrue("Transition should be valid", this.testClass.isStatusTransitionValid(AppointmentStatus.PENDING,
                AppointmentStatus.DNA));
    }

    /**
     * Tests the isStatusTransitionValid method from the IN_PROGRESS status
     */
    @Test
    public void testStatusTransitionValidationFromInProgressStatus() {
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus
                        .IN_PROGRESS,
                AppointmentStatus.NEW));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus
                        .IN_PROGRESS,
                AppointmentStatus.PENDING));
        assertTrue("Transition should be valid", this.testClass.isStatusTransitionValid(AppointmentStatus
                        .IN_PROGRESS,
                AppointmentStatus.IN_PROGRESS));
        assertTrue("Transition should be valid", this.testClass.isStatusTransitionValid(AppointmentStatus
                        .IN_PROGRESS,
                AppointmentStatus.COMPLETE));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus
                        .IN_PROGRESS,
                AppointmentStatus.CANCEL));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus
                        .IN_PROGRESS,
                AppointmentStatus.CANCELLED));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus
                        .IN_PROGRESS,
                AppointmentStatus.DNA));
    }

    /**
     * Tests the isStatusTransitionValid method from the COMPLETE status
     */
    @Test
    public void testStatusTransitionValidationFromCompleteStatus() {
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.COMPLETE,
                AppointmentStatus.NEW));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.COMPLETE,
                AppointmentStatus.PENDING));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.COMPLETE,
                AppointmentStatus.IN_PROGRESS));
        assertTrue("Transition should be valid", this.testClass.isStatusTransitionValid(AppointmentStatus.COMPLETE,
                AppointmentStatus.COMPLETE));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.COMPLETE,
                AppointmentStatus.CANCEL));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.COMPLETE,
                AppointmentStatus.CANCELLED));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.COMPLETE,
                AppointmentStatus.DNA));
    }

    /**
     * Tests the isStatusTransitionValid method from the CANCEL status
     */
    @Test
    public void testStatusTransitionValidationFromCancelStatus() {
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.CANCEL,
                AppointmentStatus.NEW));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.CANCEL,
                AppointmentStatus.PENDING));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.CANCEL,
                AppointmentStatus.IN_PROGRESS));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.CANCEL,
                AppointmentStatus.COMPLETE));
        assertTrue("Transition should be valid", this.testClass.isStatusTransitionValid(AppointmentStatus.CANCEL,
                AppointmentStatus.CANCEL));
        assertTrue("Transition should be valid", this.testClass.isStatusTransitionValid(AppointmentStatus.CANCEL,
                AppointmentStatus.CANCELLED));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.CANCEL,
                AppointmentStatus.DNA));
    }

    /**
     * Tests the isStatusTransitionValid method from the CANCELLED status
     */
    @Test
    public void testStatusTransitionValidationFromCancelledStatus() {
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.CANCELLED,
                AppointmentStatus.NEW));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.CANCELLED,
                AppointmentStatus.PENDING));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.CANCELLED,
                AppointmentStatus.IN_PROGRESS));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.CANCELLED,
                AppointmentStatus.COMPLETE));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.CANCELLED,
                AppointmentStatus.CANCEL));
        assertTrue("Transition should be valid", this.testClass.isStatusTransitionValid(AppointmentStatus.CANCELLED,
                AppointmentStatus.CANCELLED));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.CANCELLED,
                AppointmentStatus.DNA));
    }

    /**
     * Tests the isStatusTransitionValid method from the DNA status
     */
    @Test
    public void testStatusTransitionValidationFromDnaStatus() {
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.DNA,
                AppointmentStatus.NEW));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.DNA,
                AppointmentStatus.PENDING));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.DNA,
                AppointmentStatus.IN_PROGRESS));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.DNA,
                AppointmentStatus.COMPLETE));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.DNA,
                AppointmentStatus.CANCEL));
        assertFalse("Transition should be invalid", this.testClass.isStatusTransitionValid(AppointmentStatus.DNA,
                AppointmentStatus.CANCELLED));
        assertTrue("Transition should be valid", this.testClass.isStatusTransitionValid(AppointmentStatus.DNA,
                AppointmentStatus.DNA));
    }
}
