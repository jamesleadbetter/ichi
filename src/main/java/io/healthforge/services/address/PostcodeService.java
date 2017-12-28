package io.healthforge.services.address;

import io.healthforge.models.Address;
import io.healthforge.models.component.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * Component providing functionality for dealing with {@link Address} postcodes
 */
@Component
public class PostcodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostcodeService.class);

    private static final int ARGENTINA_POSTCODE_LINE = 2;
    private static final int CHINA_POSTCODE_LINE = 5;
    private static final int BELARUS_POSTCODE_LINE = 2;
    private static final int JAPAN_POSTCODE_LINE = 0;
    private static final int USA_POSTCODE_LINE = 2;

    /**
     * Determines whether the provided {@link Address} matches the given postcode
     *
     * @param address  the address to check the postcode of
     * @param postcode the postcode to match against
     * @return whether the postcode matches
     */
    public boolean matchesPostcode(final Address address, final String postcode) {
        if (address == null || !StringUtils.hasText(postcode)) {
            return false;
        }

        final Optional<Country> country = Country.getCountryFromAddress(address.getCountry());

        if (!country.isPresent()) {
            LOGGER.debug("Unable to find matching Country for value {}", address.getCountry());
            return false;
        }

        // assume postcode always on same line for each country
        final Optional<String> postcodeLine = this.extractPostcodeLine(address.getLines(), country.get());

        if (!postcodeLine.isPresent()) {
            LOGGER.debug("Did not find expected postcode line within address lines {} for country {}", address
                    .getLines(), country.get());
            return false;
        }

        return postcodeLine.get().contains(postcode);
    }

    /**
     * Extracts the expected line containing the postcode from the provided lines of an {@link Address} for a given
     * {@link Country}. {@link Optional} empty returned if line cannot be found
     *
     * @param addressLines the lines to extract the postcode from
     * @param country      the country the address relates to
     * @return the line containing the postcode
     */
    private Optional<String> extractPostcodeLine(final List<String> addressLines, final Country country) {

        if (CollectionUtils.isEmpty(addressLines)) {
            return Optional.empty();
        }

        final int postcodeLineNumber;
        switch (country) {
            case ARGENTINA:
                postcodeLineNumber = ARGENTINA_POSTCODE_LINE;
                break;
            case CHINA:
                postcodeLineNumber = CHINA_POSTCODE_LINE;
                break;
            case BELARUS:
                postcodeLineNumber = BELARUS_POSTCODE_LINE;
                break;
            case JAPAN:
                postcodeLineNumber = JAPAN_POSTCODE_LINE;
                break;
            case USA:
                postcodeLineNumber = USA_POSTCODE_LINE;
                break;
            default:
                return Optional.empty();
        }

        if (postcodeLineNumber < addressLines.size()) {
            return Optional.of(addressLines.get(postcodeLineNumber));
        }

        return Optional.empty();
    }
}