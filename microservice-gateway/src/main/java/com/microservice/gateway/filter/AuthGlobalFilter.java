package com.microservice.gateway.filter;

import com.microservice.common.constant.SecurityConstants;
import com.microservice.common.util.JwtUtils;
import com.microservice.gateway.config.AuthProperties;
import com.microservice.gateway.config.JwtProperties;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 认证全局过滤器
 * <p>
 * 负责请求日志记录、Token 校验、用户信息透传。
 * 优先级最高（order = 0），在路由之前执行。
 * </p>
 *
 * @author microservice
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    // 与权限相关的路径，以及排除拦截的路径
    private final AuthProperties authProperties;
    // JWT 配置
    private final JwtProperties jwtProperties;
    // 路径匹配，可以判断带通配符**的路径
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String method = request.getMethod() != null ? request.getMethod().name() : "UNKNOWN";

        // 1. 记录进入网关的请求信息
        log.info("[Gateway] method: {}, path: {}, from: {}", method, path, request.getRemoteAddress());

        // 2. 判断是否在白名单中
        if (isExclude(path)) {
            log.info("[Gateway] 白名单路径，直接放行: {}", path);
            return chain.filter(exchange);
        }

        boolean flag=true;
        if( flag){
            log.info("[Gateway] 暂时用于测试，路径: {} 直接放行", path);
            return chain.filter(exchange);
        }



        // 3. 获取 Token
        String token = extractToken(request);
        if (token == null) {
            log.warn("[Gateway] 请求未携带 Token: {} {}", method, path);
            return unauthorized(exchange, "未登录");
        }

        // 4. 校验并解析 Token
        // TODO: 实现 Token 校验逻辑
        //   - 使用 JwtUtils.parseToken(token, secret) 解析
        //   - 校验 Token 是否在黑名单中（查 Redis: auth:token_blacklist:{token}）
        //   - 校验 Token 版本号是否与 Redis 中一致（防踢人下线）
        //   - 解析失败返回 401
        Claims claims;
        try {
            claims = JwtUtils.parseToken(token, jwtProperties.getSecret());
        } catch (Exception e) {
            log.warn("[Gateway] Token 校验失败: {} {} - {}", method, path, e.getMessage());
            return unauthorized(exchange, "Token 无效或已过期");
        }

        // 5. 提取用户信息并透传到下游服务
        // TODO: 从 Claims 中提取用户信息，透传到下游服务的 Header
        //   - userId → X-User-Id
        //   - username → X-User-Name
        //   - roleType → X-User-Role
        //   下游服务通过 UserContextInterceptor 读取这些 Header 并设置到 UserContext
        String userId = String.valueOf(claims.get(SecurityConstants.CLAIM_USER_ID));
        String username = (String) claims.get(SecurityConstants.CLAIM_USERNAME);
        String roleType = String.valueOf(claims.get(SecurityConstants.CLAIM_ROLE_TYPE));

        log.info("[Gateway] Token 校验通过: userId={}, username={}", userId, username);

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(builder -> {
                    builder.header(SecurityConstants.HEADER_USER_ID, userId);
                    if (username != null) {
                        builder.header(SecurityConstants.HEADER_USER_NAME, username);
                    }
                    if (roleType != null) {
                        builder.header(SecurityConstants.HEADER_USER_ROLE, roleType);
                    }
                })
                .build();

        // 6. 放行
        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 判断请求路径是否在白名单中
     */
    private boolean isExclude(String path) {
        for (String pattern : authProperties.getExcludePaths()) {
            if (pathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从 Authorization 请求头中提取 Bearer Token
     */
    private String extractToken(ServerHttpRequest request) {
        List<String> headers = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (headers == null || headers.isEmpty()) {
            return null;
        }
        String bearer = headers.get(0);
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return bearer;
    }

    /**
     * 返回 401 未授权响应
     */
    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().set("Content-Type", "application/json;charset=UTF-8");
        String body = String.format("{\"code\":401,\"message\":\"%s\"}", message);
        return response.writeWith(Mono.just(
                response.bufferFactory().wrap(body.getBytes(java.nio.charset.StandardCharsets.UTF_8))
        ));
    }
}
