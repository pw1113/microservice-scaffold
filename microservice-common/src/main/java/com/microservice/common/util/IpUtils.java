package com.microservice.common.util;

import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;

/**
 * IP 工具类
 *
 * @author microservice
 */
public class IpUtils {

    private IpUtils() {
    }

    /**
     * 获取客户端真实 IP 地址
     *
     * @param request HttpServletRequest
     * @return IP 地址
     */
    public static String getClientIp(HttpServletRequest request) {
        return JakartaServletUtil.getClientIP(request);
    }

}
