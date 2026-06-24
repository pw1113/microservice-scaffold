package com.microservice.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * JWT 配置属性
 * <p>
 * 绑定 jwt.* 配置项，用于网关层 Token 校验。
 * secret 需与用户服务保持一致（HMAC-SHA256，Base64 编码）。
 * </p>
 *
 * @author microservice
 */
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /** Base64 编码的 HMAC-SHA256 密钥 */
    private String secret;

    /** AccessToken 有效期 */
    private Duration accessTokenTtl = Duration.ofMinutes(15);

    /** RefreshToken 有效期 */
    private Duration refreshTokenTtl = Duration.ofDays(3);
}
