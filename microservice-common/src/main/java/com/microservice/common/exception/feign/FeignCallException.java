package com.microservice.common.exception.feign;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.FeignResultCode;

/**
 * 远程服务调用失败异常
 *
 * @author microservice
 */
public class FeignCallException extends BusinessException {

    public FeignCallException() {
        super(FeignResultCode.FEIGN_CALL_FAILED);
    }

    public FeignCallException(String message) {
        super(FeignResultCode.FEIGN_CALL_FAILED.getCode(), message);
    }
}
