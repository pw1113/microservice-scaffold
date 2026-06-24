package com.microservice.common.handler;

import com.microservice.common.exception.ParamException;
import com.microservice.common.exception.BusinessException;
import com.microservice.common.exception.file.FileDownloadException;
import com.microservice.common.exception.file.FileSizeExceededException;
import com.microservice.common.exception.file.FileTypeNotSupportedException;
import com.microservice.common.exception.file.FileUploadException;
import com.microservice.common.result.HttpResultCode;
import com.microservice.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

/**
 * 参数校验 / 文件上传 异常处理器
 *
 * @author microservice
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ValidationExceptionHandler {

    // ======================== 400 参数错误 ========================

    @ExceptionHandler({
            ParamException.class,
            FileDownloadException.class,
            FileSizeExceededException.class,
            FileTypeNotSupportedException.class,
            FileUploadException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBadRequest(BusinessException e, HttpServletRequest request) {
        log.warn("[{}] path={}, code={}, message={}", e.getClass().getSimpleName(),
                request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("[MethodArgumentNotValid] path={}, message={}", request.getRequestURI(), message);
        return Result.fail(HttpResultCode.BAD_REQUEST.getCode(), message);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBindException(BindException e, HttpServletRequest request) {
        String message = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("[BindException] path={}, message={}", request.getRequestURI(), message);
        return Result.fail(HttpResultCode.BAD_REQUEST.getCode(), message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMissingParam(MissingServletRequestParameterException e, HttpServletRequest request) {
        log.warn("[MissingParam] path={}, param={}", request.getRequestURI(), e.getParameterName());
        return Result.fail(HttpResultCode.BAD_REQUEST.getCode(), "缺少必要参数: " + e.getParameterName());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleTypeMismatch(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.warn("[TypeMismatch] path={}, param={}", request.getRequestURI(), e.getName());
        return Result.fail(HttpResultCode.BAD_REQUEST.getCode(), "参数类型错误: " + e.getName());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMessageNotReadable(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("[MessageNotReadable] path={}", request.getRequestURI());
        return Result.fail(HttpResultCode.BAD_REQUEST.getCode(), "请求体格式错误");
    }

    // ======================== 405 方法不允许 ========================

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Void> handleMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn("[MethodNotSupported] path={}, method={}", request.getRequestURI(), e.getMethod());
        return Result.fail(HttpResultCode.METHOD_NOT_ALLOWED);
    }

}
