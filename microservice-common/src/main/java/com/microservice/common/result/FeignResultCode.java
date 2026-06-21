package com.microservice.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 远程调用相关状态码枚举 (6xxx)
 *
 * @author microservice
 */
@Getter
@AllArgsConstructor
public enum FeignResultCode implements IResultCode {

    FEIGN_CALL_FAILED(6001, "远程服务调用失败"),
    FEIGN_FALLBACK(6002, "远程服务降级");

    /** 状态码 */
    private final Integer code;

    /** 消息 */
    private final String message;
}
