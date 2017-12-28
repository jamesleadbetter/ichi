/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.services.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import io.healthforge.models.Patient;
import io.healthforge.services.PatientService;
import io.healthforge.services.address.PostcodeService;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Patient service implementation
 */
@Component
public class PatientServiceImpl extends BaseServiceImpl<Patient> implements PatientService {

    private final PostcodeService postcodeService;

    @Autowired
    public PatientServiceImpl(final PostcodeService postcodeService) {
        this.postcodeService = postcodeService;
    }

    @Override
    protected List<Patient> getDoFilter(List<Patient> items, Map<String, Object> searchParams) {
        if (searchParams.size() > 0) {
            if (searchParams.containsKey("dob")) {
                LocalDate dob = ((DateTime) searchParams.get("dob")).toLocalDate();
                items = Lists.newArrayList(Collections2.filter(items, new Predicate<Patient>() {
                    @Override
                    public boolean apply(Patient patient) {
                        if (patient.getDateOfBirth() != null && patient.getDateOfBirth().toLocalDate().isEqual(dob)) {
                            return true;
                        }
                        return false;
                    }
                }));
            }

            if (searchParams.containsKey(POSTCODE_PARAM)) {
                final String postcode = String.class.cast(searchParams.get(POSTCODE_PARAM));
                items = this.filterByPostcode(items, postcode);
            }
        }
        return items;
    }

    /**
     * Filters the provided list of patients by the provided postcode
     *
     * @param items    the list of patients to be filtered
     * @param postcode the postcode that the patients should have to match the filter
     * @return the patients matching the provided postcode
     */
    private List<Patient> filterByPostcode(final List<Patient> items, final String postcode) {
        return items.stream().filter(patient -> patient.getAddresses().stream().anyMatch(address -> this
                .postcodeService.matchesPostcode(address, postcode))).collect(Collectors.toList());
    }
}
