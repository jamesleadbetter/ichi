package io.healthforge.services.impl;

import io.healthforge.models.Address;
import io.healthforge.models.Patient;
import io.healthforge.services.PatientService;
import io.healthforge.services.address.PostcodeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

/**
 * Unit tests for the {@link PatientServiceImpl} component
 */
@RunWith(MockitoJUnitRunner.class)
public class PatientServiceImplTest {

    @Mock
    private PostcodeService mockPostcodeService;

    private PatientServiceImpl testClass;

    /**
     * Sets up the mocks before each test
     */
    @Before
    public void setup() {
        this.testClass = new PatientServiceImpl(this.mockPostcodeService);
    }

    /**
     * Tests that no items are returned when applying a postcode filter and the postcode service returns no matches
     */
    @Test
    public void shouldReturnNoItemsWhenPostcodeServiceReturnsNoMatches() {
        doReturn(false).when(this.mockPostcodeService).matchesPostcode(any(Address.class), anyString());

        final Map<String, Object> searchParams = new HashMap<>();
        searchParams.put(PatientService.POSTCODE_PARAM, "mockPostcode");

        final List<Patient> result = this.testClass.getDoFilter(this.buildMockPatients(), searchParams);

        assertTrue("Result should be empty list", result.isEmpty());
    }

    /**
     * Tests that all items are returned when applying a postcode filter and the postcode service returns everything
     * as a match
     */
    @Test
    public void shouldReturnItemsWhenPostcodeServiceReturnsMatches() {
        doReturn(true).when(this.mockPostcodeService).matchesPostcode(any(Address.class), anyString());

        final Map<String, Object> searchParams = new HashMap<>();
        searchParams.put(PatientService.POSTCODE_PARAM, "mockPostcode");

        final List<Patient> items = this.buildMockPatients();

        final List<Patient> result = this.testClass.getDoFilter(items, searchParams);

        assertEquals("All items should be returned", items, result);
    }

    /**
     * Builds a mock list of patients for testing purposes
     *
     * @return a mock list of patients
     */
    private List<Patient> buildMockPatients() {
        final Patient mockPatient = new Patient();
        mockPatient.setAddresses(Arrays.asList(new Address()));
        return Arrays.asList(mockPatient);
    }
}
