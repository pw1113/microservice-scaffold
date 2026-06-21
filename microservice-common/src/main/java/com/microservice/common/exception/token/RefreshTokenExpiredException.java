package com.microservice.common.exception.token;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.TokenResultCode;

/**
 * 刷新 Token 已过期异常
 *
 * @author microservice
 */
public class RefreshTokenExpiredException extends BusinessException {

    public RefreshTokenExpiredException() {
        super(TokenResultCode.REFRESH_TOKEN_EXPIRED);
    }

    public RefreshTokenExpiredException(String message) {
        super(TokenResultCode.REFRESH_TOKEN_EXPIRED.getCode(), message);
    }
}
