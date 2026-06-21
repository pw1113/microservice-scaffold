package com.microservice.common.exception.file;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.FileResultCode;

/**
 * 文件大小超出限制异常
 *
 * @author microservice
 */
public class FileSizeExceededException extends BusinessException {

    public FileSizeExceededException() {
        super(FileResultCode.FILE_SIZE_EXCEEDED);
    }

    public FileSizeExceededException(String message) {
        super(FileResultCode.FILE_SIZE_EXCEEDED.getCode(), message);
    }
}
