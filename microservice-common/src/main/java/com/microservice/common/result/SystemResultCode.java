package com.microservice.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统管理相关状态码枚举 (5xxx)
 *
 * @author microservice
 */
@Getter
@AllArgsConstructor
public enum SystemResultCode implements IResultCode {

    ADMIN_ROLE_REQUIRED(5001, "需要管理员权限"),
    OPERATION_LOG_SAVE_FAILED(5002, "操作日志保存失败");

    /** 状态码 */
    private final Integer code;

    /** 消息 */
    private final String message;
}
