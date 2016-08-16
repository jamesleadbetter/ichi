/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.junit.Before;
import org.springframework.boot.context.embedded.LocalServerPort;

public abstract class BaseTest {

    /**
     * Keeps the randomised port that Spring Boot starts the container on
     */
    @LocalServerPort
    protected int port;

    protected ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JodaModule());
        objectMapper.setDateFormat(new ISO8601DateFormat());
    }
}