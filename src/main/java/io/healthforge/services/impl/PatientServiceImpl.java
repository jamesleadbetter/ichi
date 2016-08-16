/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.services.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import io.healthforge.models.Patient;
import io.healthforge.services.PatientService;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Patient service implementation
 */
@Component
public class PatientServiceImpl extends BaseServiceImpl<Patient> implements PatientService {
    @Override
    protected List<Patient> getDoFilter(List<Patient> items, Map<String, Object> searchParams) {
        if(searchParams.size() > 0) {
            if(searchParams.containsKey("dob")) {
                LocalDate dob = ((DateTime)searchParams.get("dob")).toLocalDate();
                items = Lists.newArrayList(Collections2.filter(items, new Predicate<Patient>() {
                    @Override
                    public boolean apply(Patient patient) {
                        if(patient.getDateOfBirth() != null && patient.getDateOfBirth().toLocalDate().isEqual(dob)) {
                            return true;
                        }
                        return false;
                    }
                }));
            }
        }
        return items;
    }
}
