package io.healthforge;

import io.healthforge.models.GenericResponse;
import io.healthforge.models.component.AppointmentStatus;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * CRUD tests against the {@link io.healthforge.rest.AppointmentApi}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppointmentCrudTest extends BaseTest {

    private static final String BASE_URL = "http://localhost:";
    private static final String APPOINTMENT_API_PATH = "/api/appointments";

    private final RestTemplate restTemplate = new RestTemplate();

    private String url;

    /**
     * Performs setup before each test
     */
    @Before
    public void setup() {
        this.url = BASE_URL + this.port + APPOINTMENT_API_PATH;

        // default error handler causes exceptions to be thrown
        this.restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                final HttpStatus.Series series = clientHttpResponse.getStatusCode().series();
                return (HttpStatus.Series.CLIENT_ERROR.equals(series)
                        || HttpStatus.Series.SERVER_ERROR.equals(series));
            }

            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
                // do nothing
            }
        });
    }

    /**
     * Tests the create endpoint for an appointment
     */
    @Test
    public void testCreateAppointment() throws IOException {
        final AppointmentClientObject appointment = this.buildMockAppointment();
        final String jsonAppointment = this.objectMapper.writeValueAsString(appointment);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<String> entity = new HttpEntity<>(jsonAppointment, headers);

        final String responseJson = this.restTemplate.postForObject(this.url, entity, String.class);
        final AppointmentClientObject createdAppointment = this.objectMapper.readValue(responseJson,
                AppointmentClientObject.class);

        assertNotNull("Created appointment should be returned", createdAppointment);

        assertNotNull("Created appointment should be assigned an ID", createdAppointment.id);

        assertEquals("created clinician ID should match provided", appointment.clinicianId, createdAppointment
                .clinicianId);
        assertEquals("created ends at should match provided", appointment.endAt.getMillis(), createdAppointment.endAt
                .getMillis());
        assertEquals("created starts at should match provided", appointment.startsAt.getMillis(), createdAppointment
                .startsAt.getMillis());
        assertEquals("created patient ID should match provided", appointment.patientId, createdAppointment.patientId);
        assertEquals("created reason should match provided", appointment.reason, createdAppointment.reason);
        assertEquals("created status should be new", AppointmentStatus.NEW, createdAppointment.status);
    }

    /**
     * Test the get endpoint for an appointment
     */
    @Test
    public void testGetAppointment() throws IOException {
        final AppointmentClientObject appointment = this.buildMockAppointment();
        final String jsonAppointment = this.objectMapper.writeValueAsString(appointment);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<String> entity = new HttpEntity<>(jsonAppointment, headers);

        final String createdResponseJson = this.restTemplate.postForObject(this.url, entity, String.class);
        final AppointmentClientObject createdAppointment = this.objectMapper.readValue(createdResponseJson,
                AppointmentClientObject.class);

        final String getUrl = this.url + "/" + createdAppointment.id;
        final String getResponseJson = this.restTemplate.getForObject(getUrl, String.class);

        final AppointmentClientObject getAppointment = this.objectMapper.readValue(getResponseJson,
                AppointmentClientObject.class);

        assertNotNull("appointment should be returned", getAppointment);

        assertEquals("appointment ID should match", createdAppointment.id, getAppointment.id);

        assertEquals("clinician ID should match provided", appointment.clinicianId, getAppointment
                .clinicianId);
        assertEquals("ends at should match provided", appointment.endAt.getMillis(), getAppointment.endAt
                .getMillis());
        assertEquals("starts at should match provided", appointment.startsAt.getMillis(), getAppointment
                .startsAt.getMillis());
        assertEquals("patient ID should match provided", appointment.patientId, getAppointment.patientId);
        assertEquals("reason should match provided", appointment.reason, getAppointment.reason);
        assertEquals("status should be new", createdAppointment.status, getAppointment.status);

    }

    /**
     * Test the update endpoint for an appointment
     */
    @Test
    public void testUpdateAppointment() throws IOException {
        final AppointmentClientObject appointment = this.buildMockAppointment();
        final String jsonAppointment = this.objectMapper.writeValueAsString(appointment);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<String> entity = new HttpEntity<>(jsonAppointment, headers);

        final String createdResponseJson = this.restTemplate.postForObject(this.url, entity, String.class);
        final AppointmentClientObject createdAppointment = this.objectMapper.readValue(createdResponseJson,
                AppointmentClientObject.class);

        final String updateUrl = this.url + "/" + createdAppointment.id;
        final String updatedReason = "new reason";
        createdAppointment.setReason(updatedReason);

        final HttpEntity<String> updateEntity = new HttpEntity<>(this.objectMapper.writeValueAsString
                (createdAppointment), headers);
        final ResponseEntity<String> updateResponse = this.restTemplate.exchange(updateUrl, HttpMethod.PUT,
                updateEntity, String
                        .class);

        final AppointmentClientObject updatedAppointment = this.objectMapper.readValue(updateResponse.getBody(),
                AppointmentClientObject.class);

        assertEquals("Reason should be updated", updatedReason, updatedAppointment.getReason());
    }

    /**
     * Tests the deletion endpoint for an appointment
     */
    @Test
    public void testDeleteAppointment() throws IOException {
        final AppointmentClientObject appointment = this.buildMockAppointment();
        final String jsonAppointment = this.objectMapper.writeValueAsString(appointment);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<String> entity = new HttpEntity<>(jsonAppointment, headers);

        final String createdResponseJson = this.restTemplate.postForObject(this.url, entity, String.class);
        final AppointmentClientObject createdAppointment = this.objectMapper.readValue(createdResponseJson,
                AppointmentClientObject.class);

        final String deleteUrl = this.url + "/" + createdAppointment.id;
        this.restTemplate.delete(deleteUrl);

        final HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final ResponseEntity<GenericResponse> responseEntity = this.restTemplate.exchange(deleteUrl, HttpMethod.GET,
                new HttpEntity<>(getHeaders), GenericResponse.class);
        assertEquals("404 should be returned", HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    /**
     * Tests that a Bad Request response is returned when making an invalid appointment update
     */
    @Test
    public void testInvalidUpdate() throws IOException {
        final AppointmentClientObject appointment = this.buildMockAppointment();
        final String jsonAppointment = this.objectMapper.writeValueAsString(appointment);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<String> entity = new HttpEntity<>(jsonAppointment, headers);

        final String createdResponseJson = this.restTemplate.postForObject(this.url, entity, String.class);
        final AppointmentClientObject createdAppointment = this.objectMapper.readValue(createdResponseJson,
                AppointmentClientObject.class);

        final String updateUrl = this.url + "/" + createdAppointment.id;
        // invalid status transition
        createdAppointment.setStatus(AppointmentStatus.IN_PROGRESS);

        final HttpEntity<String> updateEntity = new HttpEntity<>(this.objectMapper.writeValueAsString
                (createdAppointment), headers);
        final ResponseEntity<String> updateResponse = this.restTemplate.exchange(updateUrl, HttpMethod.PUT,
                updateEntity, String
                        .class);

        assertEquals("400 should be returned", HttpStatus.BAD_REQUEST, updateResponse.getStatusCode());
    }

    /**
     * Creates an {@link AppointmentClientObject} for testing purposes
     *
     * @return the appointment
     */
    private AppointmentClientObject buildMockAppointment() {
        final AppointmentClientObject appointment = new AppointmentClientObject();
        appointment.clinicianId = UUID.randomUUID();
        appointment.endAt = DateTime.now().plusDays(1);
        appointment.startsAt = DateTime.now().plusHours(1);
        appointment.patientId = UUID.randomUUID();
        appointment.reason = "not very well";

        return appointment;
    }

    /**
     * Models an {@link io.healthforge.models.Appointment} from the client of the API perspective
     */
    private static final class AppointmentClientObject {
        private UUID id;
        private AppointmentStatus status;
        private DateTime startsAt;
        private DateTime endAt;
        private UUID patientId;
        private UUID clinicianId;
        private String reason;

        /**
         * @return the ID of the appointment
         */
        public UUID getId() {
            return id;
        }

        /**
         * Sets the ID of the appointment
         *
         * @param id the ID
         */
        public void setId(UUID id) {
            this.id = id;
        }

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
}
