package com.microservice.common.exception;

import com.microservice.common.result.HttpResultCode;
import com.microservice.common.result.IResultCode;
import lombok.Getter;

/**
 * 参数异常
 *
 * @author microservice
 */
@Getter
public class ParamException extends RuntimeException {

    private final Integer code;

    public ParamException(String message) {
        super(message);
        this.code = HttpResultCode.BAD_REQUEST.getCode();
    }

    public ParamException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ParamException(IResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

}
