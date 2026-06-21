package com.microservice.common.exception.user;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.UserResultCode;

/**
 * 用户账号已被禁用异常
 *
 * @author microservice
 */
public class UserAccountDisabledException extends BusinessException {

    public UserAccountDisabledException() {
        super(UserResultCode.USER_ACCOUNT_DISABLED);
    }

    public UserAccountDisabledException(String message) {
        super(UserResultCode.USER_ACCOUNT_DISABLED.getCode(), message);
    }
}
