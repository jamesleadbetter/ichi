/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge.config;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger configuration for Spring Fox to produce API documentation.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.healthforge"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .directModelSubstitute(DateTime.class, java.sql.Date.class)
                .directModelSubstitute(LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(LocalDateTime.class, java.util.Date.class)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "HealthForge REST API",
                "Lorem ipsum",
                "API TOS",
                "Terms of service",
                "hello@healthforge.io",
                "http://www.apache.org/licenses/LICENSE-2.0",
                "");
        return apiInfo;
    }
}
