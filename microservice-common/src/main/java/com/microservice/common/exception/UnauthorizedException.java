package com.microservice.common.exception;

import com.microservice.common.result.HttpResultCode;
import com.microservice.common.result.IResultCode;

/**
 * 未授权异常
 *
 * @author microservice
 */
public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(String message) {
        super(HttpResultCode.UNAUTHORIZED.getCode(), message);
    }

    public UnauthorizedException(Integer code, String message) {
        super(code, message);
    }

    public UnauthorizedException(IResultCode resultCode) {
        super(resultCode);
    }

}
