package com.microservice.common.exception.system;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.SystemResultCode;

/**
 * 需要管理员权限异常
 *
 * @author microservice
 */
public class AdminRoleRequiredException extends BusinessException {

    public AdminRoleRequiredException() {
        super(SystemResultCode.ADMIN_ROLE_REQUIRED);
    }

    public AdminRoleRequiredException(String message) {
        super(SystemResultCode.ADMIN_ROLE_REQUIRED.getCode(), message);
    }
}
