package com.microservice.common.exception;

import com.microservice.common.result.HttpResultCode;
import com.microservice.common.result.IResultCode;
import lombok.Getter;

/**
 * 业务异常基类
 * <p>
 * 所有业务域异常均应继承此类，支持通过 {@link IResultCode} 接口实现模块化的错误码管理。
 * </p>
 *
 * @author microservice
 */
@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = HttpResultCode.ERROR.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(IResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public BusinessException(IResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }

}
