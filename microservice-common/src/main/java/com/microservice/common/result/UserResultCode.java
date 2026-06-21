package com.microservice.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户相关状态码枚举 (1xxx)
 *
 * @author microservice
 */
@Getter
@AllArgsConstructor
public enum UserResultCode implements IResultCode {

    USER_NOT_FOUND(1001, "用户不存在"),
    USER_PASSWORD_ERROR(1002, "用户名或密码错误"),
    USER_ACCOUNT_FROZEN(1003, "账号已被冻结"),
    USER_ACCOUNT_DISABLED(1004, "账号已被禁用"),
    USER_ALREADY_EXISTS(1005, "用户已存在"),
    USER_NOT_LOGIN(1006, "用户未登录");

    /** 状态码 */
    private final Integer code;

    /** 消息 */
    private final String message;
}
