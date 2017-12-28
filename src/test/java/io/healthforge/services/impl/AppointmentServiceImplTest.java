package io.healthforge.services.impl;

import io.healthforge.exception.InvalidModelException;
import io.healthforge.exception.NotFoundException;
import io.healthforge.models.Appointment;
import io.healthforge.models.ResultSet;
import io.healthforge.models.component.AppointmentStatus;
import io.healthforge.services.AppointmentService;
import net.jodah.concurrentunit.Waiter;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Tests for the {@link AppointmentServiceImpl}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppointmentServiceImplTest {

    @Autowired
    private AppointmentServiceImpl testClass;

    /**
     * Performs cleanup from any previous tests
     */
    @Before
    public void setup() {
        // clear any previous data
        this.testClass.items = new CopyOnWriteArrayList<>();
    }

    /**
     * Tests the creation of an appointment
     */
    @Test
    public void testCreateAppointment() {
        final Appointment appointment = this.buildMockAppointment();

        final Appointment created = this.testClass.add(appointment);
        assertNotNull("Created appointment should be returned", created);
        assertNotNull("Created appointment should be assigned an ID", created.getId());

        assertEquals("created clinician ID should match provided", appointment.getClinicianId(), created
                .getClinicianId());
        assertEquals("created ends at should match provided", appointment.getEndAt(), created.getEndAt());
        assertEquals("created starts at should match provided", appointment.getStartsAt(), created.getStartsAt());
        assertEquals("created patient ID should match provided", appointment.getPatientId(), created.getPatientId());
        assertEquals("created reason should match provided", appointment.getReason(), created.getReason());
    }

    /**
     * Tests the retrieval of an appointment
     */
    @Test
    public void testGetAppointment() throws NotFoundException {
        final Appointment appointment = this.buildMockAppointment();
        final Appointment created = this.testClass.add(appointment);

        final Appointment result = this.testClass.get(created.getId());

        assertNotNull("Appointment should be returned", result);

        assertEquals("retrieved ID should match provided", appointment.getId(), result
                .getId());
        assertEquals("retrieved clinician ID should match provided", appointment.getClinicianId(), result
                .getClinicianId());
        assertEquals("retrieved ends at should match provided", appointment.getEndAt(), result.getEndAt());
        assertEquals("retrieved starts at should match provided", appointment.getStartsAt(), result.getStartsAt());
        assertEquals("retrieved patient ID should match provided", appointment.getPatientId(), result.getPatientId());
        assertEquals("retrieved reason should match provided", appointment.getReason(), result.getReason());
    }

    /**
     * Tests the retrieval of all appointments
     */
    @Test
    public void testGetAllAppointments() {
        final int numberOfAppointments = 5;
        final Set<UUID> expectedAppointmentIds = new HashSet<>();

        for (int i = 0; i < numberOfAppointments; i++) {
            final Appointment created = this.testClass.add(this.buildMockAppointment());
            expectedAppointmentIds.add(created.getId());
        }

        final ResultSet<Appointment> appointments = this.testClass.get(0, numberOfAppointments, new HashMap<>());
        assertNotNull("Appointments should be returned", appointments);

        assertEquals("Total appointments should be returned", numberOfAppointments, appointments.getTotalItems());
        assertEquals("All appointments should be returned", expectedAppointmentIds, appointments.getItems().stream()
                .map(Appointment::getId).collect(Collectors.toSet()));
    }

    /**
     * Tests the retrieval of appointments by patient ID
     */
    @Test
    public void testFilterAppointmentsByPatientId() {
        final Set<UUID> expectedAppointmentIds = new HashSet<>();
        final UUID patientId = UUID.randomUUID();
        final int limit = 10;

        for (int i = 0; i < limit; i++) {
            final boolean matchPatientId = i % 2 == 0;
            final Appointment appointment = this.buildMockAppointment();

            if (matchPatientId) {
                appointment.setPatientId(patientId);
            }

            final Appointment created = this.testClass.add(appointment);

            if (matchPatientId) {
                expectedAppointmentIds.add(created.getId());
            }
        }

        final Map<String, Object> searchParams = new HashMap<>();
        searchParams.put(AppointmentService.PATIENT_ID_PARAM, patientId.toString());
        final ResultSet<Appointment> appointments = this.testClass.get(0, limit, searchParams);
        assertNotNull("Appointments should be returned", appointments);

        assertEquals("Total appointments should be returned", limit, appointments.getTotalItems());
        assertEquals("All appointments with matching patient ID should be returned", expectedAppointmentIds,
                appointments.getItems().stream()
                        .map(Appointment::getId).collect(Collectors.toSet()));
    }

    /**
     * Tests the updating of an appointment
     */
    @Test
    public void testUpdateAppointment() throws NotFoundException, InvalidModelException {
        final Appointment appointment = this.buildMockAppointment();
        final UUID appointmentId = this.testClass.add(appointment).getId();
        final String updatedReason = "updated reason";
        final Appointment updateAppointment = this.buildMockAppointment();
        updateAppointment.setId(appointmentId);
        updateAppointment.setReason(updatedReason);
        updateAppointment.setStatus(AppointmentStatus.NEW);

        final Appointment updated = this.testClass.update(updateAppointment);

        assertEquals("Updated appointment should be returned", updatedReason, updated.getReason());
    }

    /**
     * Tests that a {@link NotFoundException} is thrown when attempting to update an appointment that does not exist
     */
    @Test(expected = NotFoundException.class)
    public void testUpdateNonExistentAppointment() throws NotFoundException, InvalidModelException {
        final Appointment appointment = this.buildMockAppointment();
        appointment.setId(UUID.randomUUID());
        this.testClass.update(appointment);
    }

    /**
     * Tests that an {@link InvalidModelException} is thrown when attempting an update for an invalid appointment
     */
    @Test(expected = InvalidModelException.class)
    public void testUpdateInvalidAppointment() throws NotFoundException, InvalidModelException {
        final Appointment appointment = this.buildMockAppointment();
        final UUID appointmentId = this.testClass.add(appointment).getId();

        final Appointment updateAppointment = this.buildMockAppointment();
        updateAppointment.setId(appointmentId);
        updateAppointment.setStatus(AppointmentStatus.IN_PROGRESS);

        this.testClass.update(updateAppointment);
    }

    /**
     * Tests the removal of an appointment
     */
    @Test
    public void testDeleteAppointment() throws NotFoundException {
        final Appointment created = this.testClass.add(this.buildMockAppointment());

        assertTrue("Appointment should be added", this.testClass.items.stream().anyMatch(app -> created.getId()
                .equals(app.getId())));

        this.testClass.remove(created.getId());

        assertFalse("Appointment should be removed", this.testClass.items.stream().anyMatch(app -> created.getId()
                .equals(app.getId())));
    }

    /**
     * Attempts to test concurrent updates to an appointment
     */
    @Test
    public void testConcurrentRequests() throws TimeoutException {
        final UUID appointmentId = this.testClass.add(this.buildMockAppointment()).getId();

        final Waiter waiter = new Waiter();

        final List<Thread> threads = new ArrayList<>();
        threads.add(new Thread(new UpdateRunnable(appointmentId, AppointmentStatus.PENDING, this.buildMockAppointment
                (), this.testClass, waiter, false)));
        threads.add(new Thread(new UpdateRunnable(appointmentId, AppointmentStatus.IN_PROGRESS, this
                .buildMockAppointment(), this.testClass, waiter, false)));
        threads.add(new Thread(new UpdateRunnable(appointmentId, AppointmentStatus.DNA, this.buildMockAppointment(),
                this.testClass, waiter, true)));
        threads.add(new Thread(new UpdateRunnable(appointmentId, AppointmentStatus.COMPLETE, this
                .buildMockAppointment(), this.testClass, waiter, false)));

        threads.forEach(Thread::start);

        waiter.await(5, TimeUnit.SECONDS, threads.size());
    }


    /**
     * Creates an {@link Appointment} for testing purposes
     *
     * @return the appointment
     */
    private Appointment buildMockAppointment() {
        final Appointment appointment = new Appointment();
        appointment.setClinicianId(UUID.randomUUID());
        appointment.setEndAt(DateTime.now().plusDays(1));
        appointment.setStartsAt(DateTime.now().plusHours(1));
        appointment.setPatientId(UUID.randomUUID());
        appointment.setReason("not very well");

        return appointment;
    }

    /**
     * {@link Runnable} implementation that attempts to test updating an appointment
     */
    private static final class UpdateRunnable implements Runnable {

        private final UUID appointmentId;
        private final AppointmentStatus updateStatus;
        private final Appointment appointment;
        private final AppointmentServiceImpl testClass;
        private final Waiter waiter;
        private final boolean expectInvalidModelException;

        /**
         * Constructs a new {@link UpdateRunnable} instance
         *
         * @param appointmentId               the ID of the appointment to attempt to update
         * @param updateStatus                the status to update the appointment with
         * @param appointment                 the appointment to update
         * @param testClass                   the service to be tested that will perform the update
         * @param waiter                      the waiter to perform assertions and notify when the thread has completed
         * @param expectInvalidModelException whether an {@link InvalidModelException} is expected to be thrown by
         *                                    the service
         */
        UpdateRunnable(final UUID appointmentId, final AppointmentStatus updateStatus, final Appointment
                appointment, final AppointmentServiceImpl testClass, final Waiter waiter, final boolean
                               expectInvalidModelException) {
            this.appointmentId = appointmentId;
            this.updateStatus = updateStatus;
            this.appointment = appointment;
            this.testClass = testClass;
            this.waiter = waiter;
            this.expectInvalidModelException = expectInvalidModelException;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            try {
                this.appointment.setId(appointmentId);
                this.appointment.setStatus(this.updateStatus);
                final Appointment updatedAppointment = this.testClass.update(this.appointment);

                if (this.expectInvalidModelException) {
                    // should have fallen into catch block
                    waiter.fail("No InvalidModelException thrown");
                }

                this.waiter.assertEquals(this.updateStatus, updatedAppointment.getStatus());
            } catch (final InvalidModelException e) {
                if (!this.expectInvalidModelException) {
                    this.waiter.fail(e.getMessage());
                }
            } catch (final NotFoundException e) {
                this.waiter.fail(e.getMessage());
            } finally {
                this.waiter.resume();
            }
        }
    }
}
