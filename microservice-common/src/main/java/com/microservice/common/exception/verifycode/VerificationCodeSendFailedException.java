package com.microservice.common.exception.verifycode;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.VerifyCodeResultCode;

/**
 * 验证码发送失败异常
 *
 * @author microservice
 */
public class VerificationCodeSendFailedException extends BusinessException {

    public VerificationCodeSendFailedException() {
        super(VerifyCodeResultCode.VERIFICATION_CODE_SEND_FAILED);
    }

    public VerificationCodeSendFailedException(String message) {
        super(VerifyCodeResultCode.VERIFICATION_CODE_SEND_FAILED.getCode(), message);
    }
}
