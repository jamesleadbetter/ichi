/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.config;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Spring MVC configuration.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.indentOutput(true).dateFormat(new ISO8601DateFormat());
        return builder;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
    }

    @Controller
    static class HomeController {
        @RequestMapping({"/", ""})
        public String index() {
            return "redirect:swagger-ui.html";
        }
    }
}
