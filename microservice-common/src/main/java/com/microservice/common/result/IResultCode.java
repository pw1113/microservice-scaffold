package com.microservice.common.result;

/**
 * 统一响应状态码接口
 * <p>
 * 所有业务域的状态码枚举均需实现此接口，实现错误码的模块化管理。
 * </p>
 *
 * @author microservice
 */
public interface IResultCode {

    /**
     * 获取状态码
     *
     * @return 状态码
     */
    Integer getCode();

    /**
     * 获取提示消息
     *
     * @return 提示消息
     */
    String getMessage();
}
