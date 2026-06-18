package com.microservice.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * TraceId 全局过滤器
 * <p>
 * 为每个请求生成唯一 TraceId，放入 MDC 和响应头，用于全链路追踪。
 * 执行顺序最高（最先执行）。
 * </p>
 *
 * @author microservice
 */
@Slf4j
@Component
public class TraceIdFilter implements GlobalFilter, Ordered {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String MDC_TRACE_ID = "traceId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 尝试从请求头获取已有 TraceId（上游传递），否则生成新的
        String headerTraceId = exchange.getRequest().getHeaders().getFirst(TRACE_ID_HEADER);
        final String traceId = (headerTraceId != null && !headerTraceId.isBlank())
                ? headerTraceId
                : UUID.randomUUID().toString().replace("-", "");

        // 放入 MDC
        MDC.put(MDC_TRACE_ID, traceId);

        // 添加到请求头，向下游透传
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(r -> r.header(TRACE_ID_HEADER, traceId))
                .build();

        // 响应头也添加 TraceId
        mutatedExchange.getResponse().getHeaders().add(TRACE_ID_HEADER, traceId);

        return chain.filter(mutatedExchange)
                .doFinally(signalType -> MDC.clear());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
