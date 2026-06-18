package com.microservice.gateway.filter;

import com.microservice.common.constant.SecurityConstants;
import com.microservice.common.util.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 鉴权全局过滤器
 * <p>
 * 白名单放行，校验 JWT 有效性及 Redis 黑名单，
 * 解析用户信息放入 Header 向下游透传，
 * 针对 /admin/** 路径强制校验管理员角色。
 * </p>
 *
 * @author microservice
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final RedisTemplate<String, Object> redisTemplate;

    public AuthFilter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 1. 白名单放行
        if (isWhiteListed(path)) {
            return chain.filter(exchange);
        }

        // 2. 获取 Token
        String token = extractToken(exchange.getRequest());
        if (token == null || token.isBlank()) {
            return unauthorized(exchange, "缺少认证Token");
        }

        // 3. 校验 Token 是否在黑名单中
        if (Boolean.TRUE.equals(redisTemplate.hasKey(SecurityConstants.REDIS_KEY_TOKEN_BLACKLIST + token))) {
            return unauthorized(exchange, "Token已被拉黑");
        }

        // 4. 解析 Claims（校验签名+过期一次完成）
        Claims claims;
        try {
            claims = JwtUtils.parseToken(token, jwtSecret);
        } catch (Exception e) {
            return unauthorized(exchange, "Token无效或已过期");
        }
        Long userId = claims.get(SecurityConstants.CLAIM_USER_ID, Long.class);
        String username = claims.get(SecurityConstants.CLAIM_USERNAME, String.class);
        Integer roleType = claims.get(SecurityConstants.CLAIM_ROLE_TYPE, Integer.class);

        // 6. 管理员路径校验
        if (path.startsWith("/admin/") && (roleType == null || roleType != 1)) {
            return forbidden(exchange, "需要管理员权限");
        }

        // 7. 透传用户信息到下游
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header(SecurityConstants.HEADER_USER_ID, String.valueOf(userId))
                .header(SecurityConstants.HEADER_USER_NAME, username)
                .header(SecurityConstants.HEADER_USER_ROLE, String.valueOf(roleType))
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    /**
     * 判断路径是否在白名单中
     */
    private boolean isWhiteListed(String path) {
        for (String pattern : SecurityConstants.WHITE_LIST) {
            if (pathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从请求中提取 Token
     */
    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * 返回 401 未授权
     */
    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        return writeJsonResponse(exchange, HttpStatus.UNAUTHORIZED,
                "{\"code\":401,\"message\":\"" + message + "\",\"data\":null}");
    }

    /**
     * 返回 403 禁止访问
     */
    private Mono<Void> forbidden(ServerWebExchange exchange, String message) {
        return writeJsonResponse(exchange, HttpStatus.FORBIDDEN,
                "{\"code\":403,\"message\":\"" + message + "\",\"data\":null}");
    }

    /**
     * 写入 JSON 响应
     */
    private Mono<Void> writeJsonResponse(ServerWebExchange exchange, HttpStatus status, String body) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = response.bufferFactory()
                .wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        // 在 TraceIdFilter 之后执行
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }

}
