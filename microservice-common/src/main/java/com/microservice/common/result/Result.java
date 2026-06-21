package com.microservice.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应结果封装
 *
 * @param <T> 数据类型
 * @author microservice
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 状态码 */
    private Integer code;

    /** 提示消息 */
    private String message;

    /** 响应数据 */
    private T data;

    // ======================== 静态成功方法 ========================

    public static <T> Result<T> success() {
        return new Result<>(HttpResultCode.SUCCESS.getCode(), HttpResultCode.SUCCESS.getMessage(), null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(HttpResultCode.SUCCESS.getCode(), HttpResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(HttpResultCode.SUCCESS.getCode(), message, data);
    }

    // ======================== 静态失败方法 ========================

    public static <T> Result<T> fail() {
        return new Result<>(HttpResultCode.ERROR.getCode(), HttpResultCode.ERROR.getMessage(), null);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(HttpResultCode.ERROR.getCode(), message, null);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> fail(IResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    public static <T> Result<T> fail(IResultCode resultCode, String message) {
        return new Result<>(resultCode.getCode(), message, null);
    }

}
