package com.microservice.common.handler;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.exception.feign.FeignCallException;
import com.microservice.common.exception.feign.FeignFallbackException;
import com.microservice.common.exception.system.OperationLogSaveFailedException;
import com.microservice.common.result.HttpResultCode;
import com.microservice.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 系统 / Feign / 兜底 异常处理器
 *
 * @author microservice
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SystemExceptionHandler {

    // ======================== 500 系统内部错误 ========================

    @ExceptionHandler(OperationLogSaveFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleOperationLogSaveFailed(OperationLogSaveFailedException e, HttpServletRequest request) {
        log.error("[OperationLogSaveFailed] path={}, message={}", request.getRequestURI(), e.getMessage(), e);
        return Result.fail(e.getCode(), e.getMessage());
    }

    // ======================== 502 Feign 调用失败 ========================

    @ExceptionHandler(FeignCallException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public Result<Void> handleFeignCall(FeignCallException e, HttpServletRequest request) {
        log.error("[FeignCallException] path={}, message={}", request.getRequestURI(), e.getMessage(), e);
        return Result.fail(e.getCode(), e.getMessage());
    }

    // ======================== 503 Feign 降级 ========================

    @ExceptionHandler(FeignFallbackException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Result<Void> handleFeignFallback(FeignFallbackException e, HttpServletRequest request) {
        log.error("[FeignFallbackException] path={}, message={}", request.getRequestURI(), e.getMessage(), e);
        return Result.fail(e.getCode(), e.getMessage());
    }

    // ======================== 业务异常兜底（500） ========================

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("[BusinessException] path={}, code={}, message={}", request.getRequestURI(), e.getCode(), e.getMessage(), e);
        return Result.fail(e.getCode(), e.getMessage());
    }

    // ======================== 未知异常兜底 ========================

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("[UnknownException] path={}, exception={}", request.getRequestURI(), e.getClass().getName(), e);
        return Result.fail(HttpResultCode.ERROR);
    }

}
