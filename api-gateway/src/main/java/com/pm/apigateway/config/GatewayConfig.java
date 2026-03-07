package com.pm.apigateway.config;

import com.pm.apigateway.filter.JwtValidationGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder, JwtValidationGatewayFilterFactory jwtValidationGatewayFilterFactory) {

        System.out.println("====== LOADING CUSTOM SECURE ROUTES ======");

        JwtValidationGatewayFilterFactory.Config filterConfig = new JwtValidationGatewayFilterFactory.Config();

        return builder.routes()
                // 1. Patient Service - NOW SECURED
                .route("patient-service-route", r -> r.path("/api/patients/**")
                        .filters(f -> f
                                .filter(jwtValidationGatewayFilterFactory.apply(filterConfig))
                                .stripPrefix(1)
                        )
                        .uri("http://patient-service:4000"))

                // 2. Patient API Docs - SECURED
                .route("api-docs-patient-route", r -> r.path("/api-docs/patients")
                        .filters(f -> f
                                .filter(jwtValidationGatewayFilterFactory.apply(filterConfig))
                                .rewritePath("/api-docs/patients", "/v3/api-docs")
                        )
                        .uri("http://patient-service:4000"))

                // 3. Auth API Docs - PUBLIC
                .route("api-docs-auth-route", r -> r.path("/api-docs/auth")
                        .filters(f -> f.rewritePath("/api-docs/auth", "/v3/api-docs"))
                        .uri("http://auth-service:4005"))

                // 4. Auth Service - PUBLIC
                .route("auth-service-route", r -> r.path("/auth/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://auth-service:4005"))
                .build();
    }
}