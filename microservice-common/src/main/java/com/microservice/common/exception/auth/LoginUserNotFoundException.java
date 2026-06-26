package com.microservice.common.exception.auth;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.AuthResultCode;

/**
 * 登录场景用户不存在异常
 * <p>
 * 仅在 login() 流程中使用，返回 401 Unauthorized。
 * 与 {@link com.microservice.common.exception.user.UserNotFoundException} 区分开，
 * 避免两个 ExceptionHandler 同时处理同一异常导致返回状态码不确定。
 * </p>
 *
 * @author microservice
 */
public class LoginUserNotFoundException extends BusinessException {

    public LoginUserNotFoundException() {
        super(AuthResultCode.LOGIN_USER_NOT_FOUND);
    }

    public LoginUserNotFoundException(String message) {
        super(AuthResultCode.LOGIN_USER_NOT_FOUND.getCode(), message);
    }
}
