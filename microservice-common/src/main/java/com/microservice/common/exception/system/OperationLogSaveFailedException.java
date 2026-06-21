package com.microservice.common.exception.system;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.SystemResultCode;

/**
 * 操作日志保存失败异常
 *
 * @author microservice
 */
public class OperationLogSaveFailedException extends BusinessException {

    public OperationLogSaveFailedException() {
        super(SystemResultCode.OPERATION_LOG_SAVE_FAILED);
    }

    public OperationLogSaveFailedException(String message) {
        super(SystemResultCode.OPERATION_LOG_SAVE_FAILED.getCode(), message);
    }
}
