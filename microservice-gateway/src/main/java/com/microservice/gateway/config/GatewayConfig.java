package com.microservice.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关路由配置
 * <p>
 * 动态路由规则：通过 Nacos 配置动态更新，
 * 此处提供基础的路由示例作为默认配置。
 * </p>
 *
 * @author microservice
 */
@Configuration
public class GatewayConfig {

    /**
     * 基础路由配置（优先级低于 Nacos 动态路由）
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth 服务路由
                .route("microservice-auth", r -> r
                        .path("/auth/**")
                        .uri("lb://microservice-auth"))
                // User 服务路由
                .route("microservice-user", r -> r
                        .path("/user/**")
                        .uri("lb://microservice-user"))
                // Admin 服务路由
                .route("microservice-admin", r -> r
                        .path("/admin/**")
                        .uri("lb://microservice-admin"))
                // Chat 服务路由
                .route("microservice-chat", r -> r
                        .path("/chat/**")
                        .uri("lb://microservice-chat"))
                .build();
    }

}
