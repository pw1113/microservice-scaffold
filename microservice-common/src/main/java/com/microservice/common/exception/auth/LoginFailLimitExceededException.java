package com.microservice.common.exception.auth;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.AuthResultCode;

/**
 * 登录失败次数超限异常
 * <p>
 * 连续登录失败 5 次后触发，账号锁定 15 分钟。
 * </p>
 *
 * @author microservice
 */
public class LoginFailLimitExceededException extends BusinessException {

    public LoginFailLimitExceededException() {
        super(AuthResultCode.LOGIN_FAIL_LIMIT_EXCEEDED);
    }

    public LoginFailLimitExceededException(String message) {
        super(AuthResultCode.LOGIN_FAIL_LIMIT_EXCEEDED.getCode(), message);
    }
}
