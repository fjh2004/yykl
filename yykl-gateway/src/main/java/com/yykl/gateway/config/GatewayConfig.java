package com.yykl.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("user_service", r -> r.path("/api/user/**")
                .filters(f -> f.stripPrefix(2))
                .uri("lb://yykl-user-service"))
            .route("resource_service", r -> r.path("/api/resource/**")
                .filters(f -> f.stripPrefix(2))
                .uri("lb://yykl-resource-service"))
            .route("order_service", r -> r.path("/api/order/**")
                .filters(f -> f.stripPrefix(2))
                .uri("lb://yykl-order-service"))
            .build();
    }
}