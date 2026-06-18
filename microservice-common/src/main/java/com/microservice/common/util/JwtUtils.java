package com.microservice.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类
 * <p>
 * 创建 AccessToken (15分钟) 和 RefreshToken (3天)，
 * 解析 Token 并验证有效性。
 * </p>
 *
 * @author microservice
 */
@Slf4j
public class JwtUtils {

    private JwtUtils() {
    }

    /**
     * 根据 Base64 编码的密钥生成 SecretKey
     *
     * @param base64Secret Base64 编码的密钥
     * @return SecretKey
     */
    private static SecretKey getSecretKey(String base64Secret) {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 创建 JWT Token
     *
     * @param claims   自定义 Claims
     * @param subject  主题（用户名）
     * @param secret   Base64 编码的密钥
     * @param expireMs 过期时间（毫秒）
     * @return JWT Token 字符串
     */
    public static String createToken(Map<String, Object> claims, String subject, String secret, long expireMs) {
        SecretKey key = getSecretKey(secret);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expireMs);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 创建 AccessToken
     *
     * @param claims 自定义 Claims
     * @param secret Base64 编码的密钥
     * @param expireSeconds 过期时间（秒）
     * @return AccessToken
     */
    public static String createAccessToken(Map<String, Object> claims, String secret, long expireSeconds) {
        return createToken(claims, (String) claims.get("username"), secret, expireSeconds * 1000);
    }

    /**
     * 创建 RefreshToken
     *
     * @param claims 自定义 Claims
     * @param secret Base64 编码的密钥
     * @param expireSeconds 过期时间（秒）
     * @return RefreshToken
     */
    public static String createRefreshToken(Map<String, Object> claims, String secret, long expireSeconds) {
        return createToken(claims, (String) claims.get("username"), secret, expireSeconds * 1000);
    }

    /**
     * 解析 Token 并返回 Claims
     *
     * @param token  JWT Token
     * @param secret Base64 编码的密钥
     * @return Claims
     * @throws JwtException 解析失败时抛出
     */
    public static Claims parseToken(String token, String secret) {
        SecretKey key = getSecretKey(secret);
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 校验 Token 是否有效
     *
     * @param token  JWT Token
     * @param secret Base64 编码的密钥
     * @return true-有效, false-无效
     */
    public static boolean validateToken(String token, String secret) {
        try {
            parseToken(token, secret);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("[JWT] Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 判断 Token 是否已过期
     *
     * @param token  JWT Token
     * @param secret Base64 编码的密钥
     * @return true-已过期, false-未过期
     */
    public static boolean isTokenExpired(String token, String secret) {
        try {
            Claims claims = parseToken(token, secret);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException e) {
            return true;
        }
    }

    /**
     * 从 Token 中获取指定 Claim
     *
     * @param token     JWT Token
     * @param secret    Base64 编码的密钥
     * @param claimName Claim 名称
     * @return Claim 值
     */
    public static Object getClaim(String token, String secret, String claimName) {
        try {
            Claims claims = parseToken(token, secret);
            return claims.get(claimName);
        } catch (JwtException e) {
            log.warn("[JWT] Failed to get claim '{}': {}", claimName, e.getMessage());
            return null;
        }
    }

}
