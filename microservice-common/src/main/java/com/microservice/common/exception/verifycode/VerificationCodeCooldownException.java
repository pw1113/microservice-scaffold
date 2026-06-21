package com.microservice.common.exception.verifycode;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.VerifyCodeResultCode;

/**
 * 验证码冷却中异常
 * <p>
 * 当用户在短时间内重复请求发送验证码时抛出此异常，
 * 用于防止验证码接口被频繁调用（防刷机制）。
 * </p>
 *
 * @author microservice
 */
public class VerificationCodeCooldownException extends BusinessException {

    public VerificationCodeCooldownException() {
        super(VerifyCodeResultCode.VERIFICATION_CODE_COOLDOWN);
    }

    public VerificationCodeCooldownException(String message) {
        super(VerifyCodeResultCode.VERIFICATION_CODE_COOLDOWN.getCode(), message);
    }
}
