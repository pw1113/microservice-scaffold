package com.microservice.common.exception.feign;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.FeignResultCode;

/**
 * 远程服务降级异常
 *
 * @author microservice
 */
public class FeignFallbackException extends BusinessException {

    public FeignFallbackException() {
        super(FeignResultCode.FEIGN_FALLBACK);
    }

    public FeignFallbackException(String message) {
        super(FeignResultCode.FEIGN_FALLBACK.getCode(), message);
    }
}
