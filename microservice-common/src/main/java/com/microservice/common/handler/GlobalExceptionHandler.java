package com.microservice.common.handler;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.exception.ParamException;
import com.microservice.common.exception.UnauthorizedException;
import com.microservice.common.result.Result;
import com.microservice.common.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author microservice
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ======================== 业务异常 ========================

    /**
     * 业务异常处理
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("[BusinessException] path={}, code={}, message={}", request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 参数异常处理
     */
    @ExceptionHandler(ParamException.class)
    public Result<Void> handleParamException(ParamException e, HttpServletRequest request) {
        log.warn("[ParamException] path={}, code={}, message={}", request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 未授权异常处理
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleUnauthorizedException(UnauthorizedException e, HttpServletRequest request) {
        log.warn("[UnauthorizedException] path={}, code={}, message={}", request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    // ======================== 参数校验异常 ========================

    /**
     * @Valid 校验失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("[MethodArgumentNotValid] path={}, message={}", request.getRequestURI(), message);
        return Result.fail(ResultCode.BAD_REQUEST.getCode(), message);
    }

    /**
     * @Validated 绑定失败
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e, HttpServletRequest request) {
        String message = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("[BindException] path={}, message={}", request.getRequestURI(), message);
        return Result.fail(ResultCode.BAD_REQUEST.getCode(), message);
    }

    /**
     * 缺少请求参数
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingParam(MissingServletRequestParameterException e, HttpServletRequest request) {
        log.warn("[MissingParam] path={}, param={}", request.getRequestURI(), e.getParameterName());
        return Result.fail(ResultCode.BAD_REQUEST.getCode(), "缺少必要参数: " + e.getParameterName());
    }

    /**
     * 参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleTypeMismatch(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.warn("[TypeMismatch] path={}, param={}", request.getRequestURI(), e.getName());
        return Result.fail(ResultCode.BAD_REQUEST.getCode(), "参数类型错误: " + e.getName());
    }

    /**
     * 数据完整性冲突（唯一键、外键约束等）
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result<Void> handleDataIntegrityViolation(DataIntegrityViolationException e, HttpServletRequest request) {
        log.warn("[DataIntegrityViolation] path={}, message={}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.CONFLICT.getCode(), "数据冲突，请检查输入信息");
    }

    /**
     * 请求体解析失败
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleMessageNotReadable(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("[MessageNotReadable] path={}", request.getRequestURI());
        return Result.fail(ResultCode.BAD_REQUEST.getCode(), "请求体格式错误");
    }

    // ======================== Web 异常 ========================

    /**
     * 404 处理
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handleNotFound(NoHandlerFoundException e, HttpServletRequest request) {
        log.warn("[NoHandlerFound] path={}", request.getRequestURI());
        return Result.fail(ResultCode.NOT_FOUND);
    }

    /**
     * 请求方法不允许
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Void> handleMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn("[MethodNotSupported] path={}, method={}", request.getRequestURI(), e.getMethod());
        return Result.fail(ResultCode.METHOD_NOT_ALLOWED);
    }

    // ======================== 兜底异常 ========================

    /**
     * 未知异常兜底处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("[UnknownException] path={}, exception={}", request.getRequestURI(), e.getClass().getName(), e);
        return Result.fail(ResultCode.ERROR);
    }

}
