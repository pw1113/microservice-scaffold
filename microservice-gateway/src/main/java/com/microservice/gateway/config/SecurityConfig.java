package com.microservice.gateway.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 安全配置
 * <p>
 * 启用 JWT 配置属性绑定。
 * JwtUtils 为静态工具类，无需注册 Bean，直接调用即可。
 * </p>
 *
 * @author microservice
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {
}
