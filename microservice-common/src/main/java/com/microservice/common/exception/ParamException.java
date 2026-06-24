package com.microservice.common.exception;

import com.microservice.common.result.HttpResultCode;
import com.microservice.common.result.IResultCode;

/**
 * 参数异常
 *
 * @author microservice
 */
public class ParamException extends BusinessException {

    public ParamException(String message) {
        super(HttpResultCode.BAD_REQUEST.getCode(), message);
    }

    public ParamException(Integer code, String message) {
        super(code, message);
    }

    public ParamException(IResultCode resultCode) {
        super(resultCode);
    }

}
