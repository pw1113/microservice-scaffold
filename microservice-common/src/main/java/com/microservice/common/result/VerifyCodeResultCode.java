package com.microservice.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码相关状态码枚举 (3xxx)
 *
 * @author microservice
 */
@Getter
@AllArgsConstructor
public enum VerifyCodeResultCode implements IResultCode {

    VERIFICATION_CODE_ERROR(3001, "验证码错误"),
    VERIFICATION_CODE_EXPIRED(3002, "验证码已过期"),
    VERIFICATION_CODE_SEND_FAILED(3003, "验证码发送失败");

    /** 状态码 */
    private final Integer code;

    /** 消息 */
    private final String message;
}
