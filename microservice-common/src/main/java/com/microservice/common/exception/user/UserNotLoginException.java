package com.microservice.common.exception.user;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.UserResultCode;

/**
 * 用户未登录异常
 *
 * @author microservice
 */
public class UserNotLoginException extends BusinessException {

    public UserNotLoginException() {
        super(UserResultCode.USER_NOT_LOGIN);
    }

    public UserNotLoginException(String message) {
        super(UserResultCode.USER_NOT_LOGIN.getCode(), message);
    }
}
