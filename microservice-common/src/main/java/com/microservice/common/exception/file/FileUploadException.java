package com.microservice.common.exception.file;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.FileResultCode;

/**
 * 文件上传失败异常
 *
 * @author microservice
 */
public class FileUploadException extends BusinessException {

    public FileUploadException() {
        super(FileResultCode.FILE_UPLOAD_FAILED);
    }

    public FileUploadException(String message) {
        super(FileResultCode.FILE_UPLOAD_FAILED.getCode(), message);
    }
}
