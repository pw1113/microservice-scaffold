package com.microservice.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件相关状态码枚举 (4xxx)
 *
 * @author microservice
 */
@Getter
@AllArgsConstructor
public enum FileResultCode implements IResultCode {

    FILE_UPLOAD_FAILED(4001, "文件上传失败"),
    FILE_DOWNLOAD_FAILED(4002, "文件下载失败"),
    FILE_SIZE_EXCEEDED(4003, "文件大小超出限制"),
    FILE_TYPE_NOT_SUPPORTED(4004, "文件类型不支持");

    /** 状态码 */
    private final Integer code;

    /** 消息 */
    private final String message;
}
