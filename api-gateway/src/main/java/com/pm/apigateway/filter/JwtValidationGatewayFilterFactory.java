package com.pm.apigateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component // this tells spring that this class is a spring bean and we want to manage it in the spring lifecycle
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> { // filter class - custom class to intercept requests and do some processing

    private final WebClient webClient; // this will be used to make requests to our auth service

    // constructor for our validationfilterfactory class
    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder, @Value("${auth.service.url}") String authServiceUrl){
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }

    // filter will be automatically applied to all the requests
    @Override
    public GatewayFilter apply(Object config){
       return (exchange, chain) -> {
           String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

           // token logic here
           if(token != null || !token.startsWith("Bearer ")){
               // check if the token is valid before proceeding to the next steps
               exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
               return exchange.getResponse().setComplete();
           }

           return webClient.get()
                   .uri("/validate")
                   .header(HttpHeaders.AUTHORIZATION, token) // set the token for the request
                   .retrieve() // retrive the response
                   .toBodilessEntity()// no body
                   .then(chain.filter(exchange)); // proceed further with request, filter has finished processing the request and can continue with next steps in the request or apply some other filter if present and required



       };
    }







}
