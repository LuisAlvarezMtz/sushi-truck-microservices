package com.luisalvarez.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class gatewayConfig {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("product-service", r -> r
                        .path("/api/v1/products/**")
                        .uri("lb://PRODUCT-SERVICE"))
                .route("inventory-service", r -> r
                        .path("/api/v1/inventory/**")
                        .uri("lb://INVENTORY-SERVICE"))
                .route("order-service", r -> r
                        .path("/api/v1/orders/**")
                        .uri("lb://ORDER-SERVICE"))
                .build();
    }
}
