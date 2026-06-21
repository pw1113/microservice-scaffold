package com.microservice.common.exception.token;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.TokenResultCode;

/**
 * Token 已被拉黑异常
 *
 * @author microservice
 */
public class TokenBlacklistedException extends BusinessException {

    public TokenBlacklistedException() {
        super(TokenResultCode.TOKEN_BLACKLISTED);
    }

    public TokenBlacklistedException(String message) {
        super(TokenResultCode.TOKEN_BLACKLISTED.getCode(), message);
    }
}
