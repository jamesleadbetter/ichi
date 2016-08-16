/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.services.impl;

import io.healthforge.models.Clinician;
import io.healthforge.services.ClinicianService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Clinician service implementation
 */
@Component
public class ClinicianServiceImpl extends BaseServiceImpl<Clinician> implements ClinicianService {
    @Override
    protected List<Clinician> getDoFilter(List<Clinician> items, Map<String, Object> searchParams) {
        return items;
    }
}
