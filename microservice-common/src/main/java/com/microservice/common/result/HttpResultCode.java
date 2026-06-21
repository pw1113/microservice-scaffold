package com.microservice.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * HTTP 标准状态码枚举
 *
 * @author microservice
 */
@Getter
@AllArgsConstructor
public enum HttpResultCode implements IResultCode {

    /** 成功 */
    SUCCESS(200, "操作成功"),

    /** 客户端错误 */
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权，请先登录"),
    FORBIDDEN(403, "无访问权限"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    CONFLICT(409, "资源冲突"),

    /** 服务端错误 */
    ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂不可用");

    /** 状态码 */
    private final Integer code;

    /** 消息 */
    private final String message;
}
