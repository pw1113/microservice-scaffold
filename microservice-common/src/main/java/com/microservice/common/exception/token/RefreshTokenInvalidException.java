package com.microservice.common.exception.token;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.TokenResultCode;

/**
 * 刷新 Token 无效异常
 *
 * @author microservice
 */
public class RefreshTokenInvalidException extends BusinessException {

    public RefreshTokenInvalidException() {
        super(TokenResultCode.REFRESH_TOKEN_INVALID);
    }

    public RefreshTokenInvalidException(String message) {
        super(TokenResultCode.REFRESH_TOKEN_INVALID.getCode(), message);
    }
}
