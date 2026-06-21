package com.microservice.common.exception.file;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.FileResultCode;

/**
 * 文件下载失败异常
 *
 * @author microservice
 */
public class FileDownloadException extends BusinessException {

    public FileDownloadException() {
        super(FileResultCode.FILE_DOWNLOAD_FAILED);
    }

    public FileDownloadException(String message) {
        super(FileResultCode.FILE_DOWNLOAD_FAILED.getCode(), message);
    }
}
