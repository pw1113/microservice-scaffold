package com.microservice.common.exception.verifycode;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.VerifyCodeResultCode;

/**
 * 验证码已过期异常
 *
 * @author microservice
 */
public class VerificationCodeExpiredException extends BusinessException {

    public VerificationCodeExpiredException() {
        super(VerifyCodeResultCode.VERIFICATION_CODE_EXPIRED);
    }

    public VerificationCodeExpiredException(String message) {
        super(VerifyCodeResultCode.VERIFICATION_CODE_EXPIRED.getCode(), message);
    }
}
