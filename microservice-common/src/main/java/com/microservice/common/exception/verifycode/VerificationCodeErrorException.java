package com.microservice.common.exception.verifycode;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.VerifyCodeResultCode;

/**
 * 验证码错误异常
 *
 * @author microservice
 */
public class VerificationCodeErrorException extends BusinessException {

    public VerificationCodeErrorException() {
        super(VerifyCodeResultCode.VERIFICATION_CODE_ERROR);
    }

    public VerificationCodeErrorException(String message) {
        super(VerifyCodeResultCode.VERIFICATION_CODE_ERROR.getCode(), message);
    }
}
