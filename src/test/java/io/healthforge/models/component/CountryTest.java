package io.healthforge.models.component;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link Country} enum
 */
public class CountryTest {

    /**
     * Tests that an {@link Optional} empty is returned when getting a Country for a null address text value
     */
    @Test
    public void shouldReturnOptionalEmptyWhenGetCountryFromAddressForNullText() {
        final Optional<Country> result = Country.getCountryFromAddress(null);

        assertFalse("Result should be empty", result.isPresent());
    }

    /**
     * Tests that an {@link Optional} empty is returned when getting a Country for an empty address text value
     */
    @Test
    public void shouldReturnOptionalEmptyWhenGetCountryFromAddressForEmptyText() {
        final Optional<Country> result = Country.getCountryFromAddress("");

        assertFalse("Result should be empty", result.isPresent());
    }

    /**
     * Tests that an {@link Optional} empty is returned when getting a Country for an address text value that does
     * not match a known Country
     */
    @Test
    public void shouldReturnOptionalEmptyWhenGetCountryFromAddressForInvalidText() {
        final Optional<Country> result = Country.getCountryFromAddress("notArealCountry");

        assertFalse("Result should be empty", result.isPresent());
    }

    /**
     * Tests that the correct {@link Country} is returned when getting a country for a known country address text value
     */
    @Test
    public void shouldReturnCountryWhenGetCountryFromAddressForValidText() {
        final Optional<Country> result = Country.getCountryFromAddress("Japan");

        assertTrue("Result should not be empty", result.isPresent());
        assertEquals("Result should be correct Country", Country.JAPAN, result.get());
    }

    /**
     * Tests that the correct {@link Country} is returned when getting a country for a known country address text
     * value (case insensitive)
     */
    @Test
    public void shouldReturnCountryWhenGetCountryFromAddressForValidTextCaseInsensitive() {
        final Optional<Country> result = Country.getCountryFromAddress("JAPAN");

        assertTrue("Result should not be empty", result.isPresent());
        assertEquals("Result should be correct Country", Country.JAPAN, result.get());
    }

    /**
     * Tests that the correct {@link Country} is returned when getting a country for a known country address text
     * value that contains non ASCII characters
     */
    @Test
    public void shouldReturnCountryWhenGetCountryFromAddressForNonAsciiText() {
        final Optional<Country> result = Country.getCountryFromAddress("Беларусь");

        assertTrue("Result should not be empty", result.isPresent());
        assertEquals("Result should be correct Country", Country.BELARUS, result.get());
    }
}
