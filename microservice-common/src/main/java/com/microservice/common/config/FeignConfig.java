package com.microservice.common.config;

import com.microservice.common.constant.SecurityConstants;
import com.microservice.common.context.UserContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * OpenFeign 配置
 * <p>
 * 配置 RequestInterceptor 透传 TraceId 和用户上下文 Header 到下游服务。
 * </p>
 *
 * @author microservice
 */
@Slf4j
@Configuration
@ConditionalOnClass(RequestInterceptor.class)
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return (RequestTemplate template) -> {
            // 1. 从 MDC 透传 TraceId
            String traceId = MDC.get("traceId");
            if (traceId != null && !traceId.isBlank()) {
                template.header(SecurityConstants.HEADER_TRACE_ID, traceId);
            }

            // 2. 从当前请求透传用户上下文 Header（如果在 Web 环境下）
            Optional<ServletRequestAttributes> attrs = Optional.ofNullable(
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
            if (attrs.isPresent()) {
                HttpServletRequest request = attrs.get().getRequest();
                copyHeader(request, template, SecurityConstants.HEADER_USER_ID);
                copyHeader(request, template, SecurityConstants.HEADER_USER_NAME);
                copyHeader(request, template, SecurityConstants.HEADER_USER_ROLE);
            } else {
                // 非 Web 环境（如定时任务），从 ThreadLocal 获取
                setIfNotNull(template, SecurityConstants.HEADER_USER_ID, UserContext.getUserId());
                setIfNotNull(template, SecurityConstants.HEADER_USER_NAME, UserContext.getUserName());
                setIfNotNull(template, SecurityConstants.HEADER_USER_ROLE, UserContext.getRoleType());
            }
        };
    }

    private void copyHeader(HttpServletRequest request, RequestTemplate template, String headerName) {
        String value = request.getHeader(headerName);
        if (value != null && !value.isBlank()) {
            template.header(headerName, value);
        }
    }

    private void setIfNotNull(RequestTemplate template, String headerName, Object value) {
        if (value != null) {
            template.header(headerName, String.valueOf(value));
        }
    }

}
