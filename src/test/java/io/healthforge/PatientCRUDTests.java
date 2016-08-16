/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge;

import io.healthforge.models.Patient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PatientCRUDTests extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(PatientCRUDTests.class);

    @Test
    public void testPatients() throws Exception {

        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.getForObject("http://localhost:" + port + "/api/patient", String.class);

        Patient patient = objectMapper.readValue(objectMapper.readTree(response).at("/items/0").traverse(), Patient.class);
        UUID patientId = patient.getId();

        logger.info("Patients[0], PatientId = {}", patientId);

        String response2 = restTemplate.getForObject("http://localhost:" + port + "/api/patient/" + patientId.toString(), String.class);
        Patient patient2 = objectMapper.readValue(response2, Patient.class);

        Assert.assertEquals(patient.getId(), patient2.getId());
        Assert.assertEquals(patient.getFirstName(), patient2.getFirstName());
        Assert.assertEquals(patient.getLastName(), patient2.getLastName());
    }
}