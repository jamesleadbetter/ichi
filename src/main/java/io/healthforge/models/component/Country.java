package io.healthforge.models.component;

import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Models the {@link Country} as part of an {@link io.healthforge.models.Address}
 */
public enum Country {
    ARGENTINA("Argentina"), CHINA("People's Republic of China"), BELARUS("Беларусь"), JAPAN("Japan"), USA("USA");

    private final String addressText;

    /**
     * Contructs a new {@link Country}
     *
     * @param addressText the text value for this country within an address
     */
    Country(final String addressText) {
        this.addressText = addressText;
    }

    /**
     * Gets the corresponding {@link Country} for a country value within an {@link io.healthforge.models.Address}.
     * {@link Optional} empty returned if no matching {@link Country} exists.
     *
     * @param addressText the value of the country from the address
     * @return the country
     */
    public static Optional<Country> getCountryFromAddress(final String addressText) {
        if (!StringUtils.hasText(addressText)) {
            return Optional.empty();
        }

        return Stream.of(Country.values()).filter(c -> c.addressText.equalsIgnoreCase(addressText)).findFirst();
    }
}
