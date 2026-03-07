package com.pm.apigateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtValidationGatewayFilterFactory.Config> {

    private final WebClient webClient;

    // FIX: Removed WebClient.Builder from the parameters.
    // Spring will now only inject the authServiceUrl string.
    public JwtValidationGatewayFilterFactory(@Value("${auth.service.url}") String authServiceUrl){
        super(Config.class);

        // FIX: Instantiate the WebClient directly without relying on Spring's dependency injection
        this.webClient = WebClient.create(authServiceUrl);
    }

    @Override
    public GatewayFilter apply(Config config){
        return (exchange, chain) -> {

            System.out.println("\n====== 1. GATEWAY FILTER TRIGGERED ======");
            System.out.println("Target URI: " + exchange.getRequest().getURI().getPath());

            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            System.out.println("Extracted Token Header: [" + token + "]");

            if(token == null || !token.startsWith("Bearer ")){
                System.out.println("====== 2. FAILED: TOKEN MISSING OR MALFORMED ======");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            System.out.println("====== 3. TOKEN FOUND. CALLING AUTH SERVICE... ======");

            return webClient.get()
                    .uri("/validate")
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .toBodilessEntity()
                    .flatMap(response -> {
                        System.out.println("====== 4. AUTH SERVICE APPROVED! FORWARDING TO PATIENT SERVICE ====== \n");
                        return chain.filter(exchange);
                    })
                    .onErrorResume(error -> {
                        System.out.println("====== 4. FAILED: AUTH SERVICE REJECTED THE TOKEN ======");
                        System.out.println("Reason: " + error.getMessage());
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        };
    }

    public static class Config{
        // Empty configuration class required by AbstractGatewayFilterFactory
    }
}