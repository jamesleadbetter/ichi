package io.healthforge.services.address;

import io.healthforge.models.Address;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

/**
 * Unit tests for the {@link PostcodeService} component
 */
@RunWith(MockitoJUnitRunner.class)
public class PostcodeServiceTest {

    private static final String JAPAN_COUNTRY = "Japan";
    private static final String USA_COUNTRY = "USA";

    @Mock
    private Address mockAddress;

    private PostcodeService testClass;

    /**
     * Sets up the mocks before each test
     */
    @Before
    public void setup() {
        this.testClass = new PostcodeService();
    }

    /**
     * Tests that a postcode is deemed to not match a null {@link Address}
     */
    @Test
    public void shouldNotMatchPostcodeForNullAddress() {
        final boolean result = this.testClass.matchesPostcode(null, "mockPostcode");

        assertFalse("Postcode should not match", result);
    }

    /**
     * Tests that a null postcode is deemed to not match an {@link Address}
     */
    @Test
    public void shouldNotMatchPostcodeForNullPostcode() {
        final boolean result = this.testClass.matchesPostcode(this.mockAddress, null);

        assertFalse("Postcode should not match", result);
    }

    /**
     * Tests that an empty postcode is deemed to not match an {@link Address}
     */
    @Test
    public void shouldNotMatchPostcodeForEmptyPostcode() {
        final boolean result = this.testClass.matchesPostcode(this.mockAddress, "");

        assertFalse("Postcode should not match", result);
    }

    /**
     * Tests that a postcode is deemed to not match an {@link Address} without a Country
     */
    @Test
    public void shouldNotMatchPostcodeForAddressWithNoCountry() {
        doReturn(null).when(this.mockAddress).getCountry();

        final boolean result = this.testClass.matchesPostcode(this.mockAddress, "mockPostcode");

        assertFalse("Postcode should not match", result);
    }

    /**
     * Tests that a postcode is deemed to not match an {@link Address} that contains no lines
     */
    @Test
    public void shouldNotMatchPostcodeForAddressWithNoLines() {
        doReturn(JAPAN_COUNTRY).when(this.mockAddress).getCountry();
        doReturn(null).when(this.mockAddress).getLines();

        final boolean result = this.testClass.matchesPostcode(this.mockAddress, "mockPostcode");

        assertFalse("Postcode should not match", result);
    }

    /**
     * Tests that a postcode is deemed to not match an {@link Address} that has an unknown Country value
     */
    @Test
    public void shouldNotMatchPostcodeForAddressWithUnknownCountry() {
        doReturn("mockCountry").when(this.mockAddress).getCountry();
        final List<String> addressLines = Arrays.asList("mockAddress1", "mockAddress2");
        doReturn(addressLines).when(this.mockAddress).getLines();

        final boolean result = this.testClass.matchesPostcode(this.mockAddress, "mockPostcode");

        assertFalse("Postcode should not match", result);
    }

    /**
     * Tests that a postcode is deemed to not match an {@link Address} that does not contain the address line for the
     * postcode for that country
     */
    @Test
    public void shouldNotMatchPostcodeForAddressWithoutExpectedLine() {
        doReturn(USA_COUNTRY).when(this.mockAddress).getCountry();
        // USA expects postcode on 3rd line
        final List<String> addressLines = Arrays.asList("mockAddress1", "mockAddress2");
        doReturn(addressLines).when(this.mockAddress).getLines();

        final boolean result = this.testClass.matchesPostcode(this.mockAddress, "mockPostcode");

        assertFalse("Postcode should not match", result);
    }

    /**
     * Tests that a postcode that is not contained within the provided {@link Address} is deemed not to match
     */
    @Test
    public void shouldNotMatchPostcodeForAddressWithoutProvidedPostcode() {
        doReturn(JAPAN_COUNTRY).when(this.mockAddress).getCountry();

        final String postcode = "mockPostcode";
        final String addressLineWithPostcode = "mockAddress1";
        final List<String> addressLines = Arrays.asList(addressLineWithPostcode);
        doReturn(addressLines).when(this.mockAddress).getLines();

        final boolean result = this.testClass.matchesPostcode(this.mockAddress, postcode);

        assertFalse("Postcode should not match", result);
    }

    /**
     * Tests that a postcode that is within the provided {@link Address} but not on the expected line is deemed not
     * to match
     */
    @Test
    public void shouldNotMatchPostcodeForAddressWithProvidedPostcodeOnDifferentLine() {
        doReturn(JAPAN_COUNTRY).when(this.mockAddress).getCountry();

        final String postcode = "mockPostcode";
        final String addressLineWithPostcode = "mockAddress1";
        final List<String> addressLines = Arrays.asList(addressLineWithPostcode, postcode);
        doReturn(addressLines).when(this.mockAddress).getLines();

        final boolean result = this.testClass.matchesPostcode(this.mockAddress, postcode);

        assertFalse("Postcode should not match", result);
    }

    /**
     * Tests that a postcode that correctly matches an {@link Address} is deemed as matching
     */
    @Test
    public void shouldMatchPostcodeForAddressWithProvidedPostcode() {
        doReturn(JAPAN_COUNTRY).when(this.mockAddress).getCountry();

        final String postcode = "mockPostcode";
        final List<String> addressLines = Arrays.asList(postcode);
        doReturn(addressLines).when(this.mockAddress).getLines();

        final boolean result = this.testClass.matchesPostcode(this.mockAddress, postcode);

        assertTrue("Postcode should match", result);
    }

    /**
     * Tests that a postcode that correctly matches an {@link Address} with additional text included on the line
     * containing the postcode, is deemed as matching
     */
    @Test
    public void shouldMatchPostcodeForAddressWithOtherTextOnPostcodeLine() {
        doReturn(JAPAN_COUNTRY).when(this.mockAddress).getCountry();

        final String postcode = "P05TC0D3";
        final String addressLineWithPostcode = String.format("some other text %s and the postcode", postcode);
        final List<String> addressLines = Arrays.asList(addressLineWithPostcode);
        doReturn(addressLines).when(this.mockAddress).getLines();

        final boolean result = this.testClass.matchesPostcode(this.mockAddress, postcode);

        assertTrue("Postcode should match", result);
    }

    /**
     * Tests that a postcode is correctly deemed as matching an {@link Address} that contains non ASCII characters
     */
    @Test
    public void shouldMatchPostcodeForAddressWithNonAsciiCharacters() {
        doReturn(JAPAN_COUNTRY).when(this.mockAddress).getCountry();

        final String postcode = "112-0001";
        final String addressLineWithPostcode = String.format("日本国 〒%s", postcode);
        final List<String> addressLines = Arrays.asList(addressLineWithPostcode);
        doReturn(addressLines).when(this.mockAddress).getLines();

        final boolean result = this.testClass.matchesPostcode(this.mockAddress, postcode);

        assertTrue("Postcode should match", result);
    }
}
