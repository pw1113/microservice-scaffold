package com.microservice.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Token 相关状态码枚举 (2xxx)
 *
 * @author microservice
 */
@Getter
@AllArgsConstructor
public enum TokenResultCode implements IResultCode {

    TOKEN_EXPIRED(2001, "Token已过期"),
    TOKEN_INVALID(2002, "Token无效"),
    TOKEN_BLACKLISTED(2003, "Token已被拉黑"),
    REFRESH_TOKEN_EXPIRED(2004, "刷新Token已过期"),
    REFRESH_TOKEN_INVALID(2005, "刷新Token无效");

    /** 状态码 */
    private final Integer code;

    /** 消息 */
    private final String message;
}
