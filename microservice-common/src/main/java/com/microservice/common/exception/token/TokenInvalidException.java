package com.microservice.common.exception.token;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.TokenResultCode;

/**
 * Token 无效异常
 *
 * @author microservice
 */
public class TokenInvalidException extends BusinessException {

    public TokenInvalidException() {
        super(TokenResultCode.TOKEN_INVALID);
    }

    public TokenInvalidException(String message) {
        super(TokenResultCode.TOKEN_INVALID.getCode(), message);
    }
}
