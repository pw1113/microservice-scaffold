package com.microservice.common.exception.user;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.UserResultCode;

/**
 * 用户密码错误异常
 *
 * @author microservice
 */
public class UserPasswordErrorException extends BusinessException {

    public UserPasswordErrorException() {
        super(UserResultCode.USER_PASSWORD_ERROR);
    }

    public UserPasswordErrorException(String message) {
        super(UserResultCode.USER_PASSWORD_ERROR.getCode(), message);
    }
}
