package com.microservice.common.exception;

import com.microservice.common.result.ResultCode;
import lombok.Getter;

/**
 * 未授权异常
 *
 * @author microservice
 */
@Getter
public class UnauthorizedException extends RuntimeException {

    private final Integer code;

    public UnauthorizedException(String message) {
        super(message);
        this.code = ResultCode.UNAUTHORIZED.getCode();
    }

    public UnauthorizedException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public UnauthorizedException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

}
