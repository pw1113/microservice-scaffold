package com.microservice.common.exception.user;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.UserResultCode;

/**
 * 用户不存在异常
 *
 * @author microservice
 */
public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super(UserResultCode.USER_NOT_FOUND);
    }

    public UserNotFoundException(String message) {
        super(UserResultCode.USER_NOT_FOUND.getCode(), message);
    }
}
