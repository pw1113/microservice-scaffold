package com.microservice.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 认证相关状态码枚举 (2xxx)
 *
 * @author microservice
 */
@Getter
@AllArgsConstructor
public enum AuthResultCode implements IResultCode {

    LOGIN_FAIL_LIMIT_EXCEEDED(2001, "登录失败次数过多，账号已锁定15分钟"),
    TOKEN_INVALID(2002, "Token无效或已过期"),
    TOKEN_BLACKLISTED(2003, "Token已被注销"),
    REFRESH_TOKEN_INVALID(2004, "RefreshToken无效或已过期"),
    REFRESH_TOKEN_BLACKLISTED(2005, "RefreshToken已被注销");

    /** 状态码 */
    private final Integer code;

    /** 消息 */
    private final String message;
}
