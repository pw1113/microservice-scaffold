package com.microservice.common.interceptor;

import com.microservice.common.constant.SecurityConstants;
import com.microservice.common.context.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 用户上下文拦截器
 * <p>
 * 从网关透传的 HTTP Header 中解析用户信息，
 * 设置到 {@link UserContext} ThreadLocal 中，
 * 请求结束后自动清理。
 * </p>
 *
 * @author microservice
 */
@Slf4j
public class UserContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = request.getHeader(SecurityConstants.HEADER_USER_ID);
        String userName = request.getHeader(SecurityConstants.HEADER_USER_NAME);
        String roleType = request.getHeader(SecurityConstants.HEADER_USER_ROLE);

        if (userId != null) {
            UserContext.setUserId(Long.parseLong(userId));
        }
        if (userName != null) {
            UserContext.setUserName(userName);
        }
        if (roleType != null) {
            UserContext.setRoleType(Integer.parseInt(roleType));
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        UserContext.clear();
    }

}
