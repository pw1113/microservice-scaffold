package com.microservice.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一响应状态码枚举
 *
 * @author microservice
 * @deprecated 已拆分为各业务域独立的状态码枚举，请使用 {@link HttpResultCode}、{@link UserResultCode}、
 *             {@link TokenResultCode}、{@link VerifyCodeResultCode}、{@link FileResultCode}、
 *             {@link SystemResultCode}、{@link FeignResultCode} 替代。
 *             为保持向后兼容，暂时保留此类。
 */
@Deprecated
@Getter
@AllArgsConstructor
public enum ResultCode implements IResultCode {

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
    SERVICE_UNAVAILABLE(503, "服务暂不可用"),

    /** 业务错误 (1xxx) */
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_PASSWORD_ERROR(1002, "用户名或密码错误"),
    USER_ACCOUNT_FROZEN(1003, "账号已被冻结"),
    USER_ACCOUNT_DISABLED(1004, "账号已被禁用"),
    USER_ALREADY_EXISTS(1005, "用户已存在"),
    USER_NOT_LOGIN(1006, "用户未登录"),

    /** Token 相关 (2xxx) */
    TOKEN_EXPIRED(2001, "Token已过期"),
    TOKEN_INVALID(2002, "Token无效"),
    TOKEN_BLACKLISTED(2003, "Token已被拉黑"),
    REFRESH_TOKEN_EXPIRED(2004, "刷新Token已过期"),
    REFRESH_TOKEN_INVALID(2005, "刷新Token无效"),

    /** 验证码相关 (3xxx) */
    VERIFICATION_CODE_ERROR(3001, "验证码错误"),
    VERIFICATION_CODE_EXPIRED(3002, "验证码已过期"),
    VERIFICATION_CODE_SEND_FAILED(3003, "验证码发送失败"),

    /** 文件相关 (4xxx) */
    FILE_UPLOAD_FAILED(4001, "文件上传失败"),
    FILE_DOWNLOAD_FAILED(4002, "文件下载失败"),
    FILE_SIZE_EXCEEDED(4003, "文件大小超出限制"),
    FILE_TYPE_NOT_SUPPORTED(4004, "文件类型不支持"),

    /** 系统管理相关 (5xxx) */
    ADMIN_ROLE_REQUIRED(5001, "需要管理员权限"),
    OPERATION_LOG_SAVE_FAILED(5002, "操作日志保存失败"),

    /** 远程调用相关 (6xxx) */
    FEIGN_CALL_FAILED(6001, "远程服务调用失败"),
    FEIGN_FALLBACK(6002, "远程服务降级");

    /** 状态码 */
    private final Integer code;

    /** 消息 */
    private final String message;

}
