package com.microservice.user.service.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT 配置类
 * <p>
 * 启用 JwtProperties 配置绑定。
 * </p>
 *
 * @author microservice
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {
}
