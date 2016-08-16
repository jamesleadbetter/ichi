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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PatientDOBTests extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(PatientDOBTests.class);

    @Test
    public void testDOBSearch() throws Exception {

        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromUriString("http://localhost:" + port + "/api/patient").queryParam("dob", "14 Feb 1915").toUriString();
        String response = restTemplate.getForObject(url, String.class);

        Patient patient = objectMapper.readValue(objectMapper.readTree(response).at("/items/0").traverse(), Patient.class);
        UUID patientId = patient.getId();

        logger.info("DOB, PatientId = {}", patientId);

        Assert.assertEquals("Patient", patient.getFirstName());
        Assert.assertEquals("Delta", patient.getLastName());
    }
}