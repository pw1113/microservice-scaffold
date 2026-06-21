package com.microservice.common.exception.user;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.UserResultCode;

/**
 * 用户账号已被冻结异常
 *
 * @author microservice
 */
public class UserAccountFrozenException extends BusinessException {

    public UserAccountFrozenException() {
        super(UserResultCode.USER_ACCOUNT_FROZEN);
    }

    public UserAccountFrozenException(String message) {
        super(UserResultCode.USER_ACCOUNT_FROZEN.getCode(), message);
    }
}
