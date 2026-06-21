package com.microservice.common.exception.token;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.TokenResultCode;

/**
 * Token 已过期异常
 *
 * @author microservice
 */
public class TokenExpiredException extends BusinessException {

    public TokenExpiredException() {
        super(TokenResultCode.TOKEN_EXPIRED);
    }

    public TokenExpiredException(String message) {
        super(TokenResultCode.TOKEN_EXPIRED.getCode(), message);
    }
}
