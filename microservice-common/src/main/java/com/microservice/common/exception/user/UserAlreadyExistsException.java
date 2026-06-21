package com.microservice.common.exception.user;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.UserResultCode;

/**
 * 用户已存在异常
 *
 * @author microservice
 */
public class UserAlreadyExistsException extends BusinessException {

    public UserAlreadyExistsException() {
        super(UserResultCode.USER_ALREADY_EXISTS);
    }

    public UserAlreadyExistsException(String message) {
        super(UserResultCode.USER_ALREADY_EXISTS.getCode(), message);
    }
}
