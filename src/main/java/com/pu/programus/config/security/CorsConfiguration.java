package com.pu.programus.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class CorsConfiguration implements WebMvcConfigurer {

    @Value("${security.cors.mapping}")
    private String securityCorsMapping;
    @Value("${security.cors.allowed.origins}")
    private String[] securityCorsAllowedOrigins;
    @Value("${security.cors.allowed.methods}")
    private String[] securityCorsAllowedMethods;
    @Value("${security.cors.allowed.headers}")
    private String[] securityCorsAllowedHeaders;
    @Value("${security.cors.maxage}")
    private Long securityCorsMaxAge;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(securityCorsMapping)
                .allowedOrigins(securityCorsAllowedOrigins)
                .allowedMethods(securityCorsAllowedMethods)
                .allowedHeaders(securityCorsAllowedHeaders)
                .maxAge(securityCorsMaxAge);
    }
}
