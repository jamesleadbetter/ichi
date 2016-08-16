/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge;

import io.healthforge.models.Address;
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

import java.io.IOException;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PatientAddressPostcodeTests extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(PatientAddressPostcodeTests.class);

    @Test
    public void testPostcodeSearch() throws Exception {

        RestTemplate restTemplate = new RestTemplate();

        testPostcode(restTemplate, "C1000WAM", "Argentina", "Patient", "Alpha");
        testPostcode(restTemplate, "B1000TBU", "Argentina", "Patient", "Alpha");
        testPostcode(restTemplate, "417009", "People's Republic of China", "Patient", "Beta");
        testPostcode(restTemplate, "223016", "Беларусь", "Patient", "Gamma");
        testPostcode(restTemplate, "112-0001", "Japan", "Patient", "Delta");
        testPostcode(restTemplate, "90210", "USA", "Patient", "Phi", true);
        testPostcode(restTemplate, "90210", "USA", "Patient", "Epsilon");
    }

    private void testPostcode(RestTemplate restTemplate, String postcode, String country, String firstName, String lastName) throws IOException {
        testPostcode(restTemplate, postcode, country, firstName, lastName, false);
    }

    private void testPostcode(RestTemplate restTemplate, String postcode, String country, String firstName, String lastName, boolean negativeTest) throws IOException {
        String url = UriComponentsBuilder.fromUriString("http://localhost:" + port + "/api/patient").queryParam("postcode", postcode).toUriString();
        String response = restTemplate.getForObject(url, String.class);

        Patient patient = objectMapper.readValue(objectMapper.readTree(response).at("/items/0").traverse(), Patient.class);
        UUID patientId = patient.getId();

        logger.info("Postcode, PatientId = {}", patientId);

        boolean countryMatch = false;
        for(Address address : patient.getAddresses()) {
            if(address.getCountry().equals(country)) {
                countryMatch = true;
                break;
            }
        }

        if(negativeTest) {
            Assert.assertEquals(firstName, patient.getFirstName());
            Assert.assertNotEquals(lastName, patient.getLastName());
        } else {
            Assert.assertEquals(firstName, patient.getFirstName());
            Assert.assertEquals(lastName, patient.getLastName());
        }
        Assert.assertTrue(countryMatch);
    }
}