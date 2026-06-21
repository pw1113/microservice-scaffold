package com.microservice.common.exception.file;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.FileResultCode;

/**
 * 文件类型不支持异常
 *
 * @author microservice
 */
public class FileTypeNotSupportedException extends BusinessException {

    public FileTypeNotSupportedException() {
        super(FileResultCode.FILE_TYPE_NOT_SUPPORTED);
    }

    public FileTypeNotSupportedException(String message) {
        super(FileResultCode.FILE_TYPE_NOT_SUPPORTED.getCode(), message);
    }
}
