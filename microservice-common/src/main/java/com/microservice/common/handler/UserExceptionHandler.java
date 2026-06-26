package com.microservice.common.handler;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.exception.user.UserAccountDisabledException;
import com.microservice.common.exception.user.UserAccountFrozenException;
import com.microservice.common.exception.user.UserAlreadyExistsException;
import com.microservice.common.exception.user.UserNotFoundException;
import com.microservice.common.exception.system.AdminRoleRequiredException;
import com.microservice.common.result.HttpResultCode;
import com.microservice.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 用户 / 权限 / 资源 异常处理器
 *
 * @author microservice
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class UserExceptionHandler {

    // ======================== 403 权限不足 ========================

    @ExceptionHandler({
            AdminRoleRequiredException.class,
            UserAccountFrozenException.class,
            UserAccountDisabledException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Void> handleForbidden(BusinessException e, HttpServletRequest request) {
        log.warn("[{}] path={}, message={}", e.getClass().getSimpleName(), request.getRequestURI(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    // ======================== 404 资源不存在 ========================

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handleUserNotFound(UserNotFoundException e, HttpServletRequest request) {
        log.warn("[UserNotFoundException] path={}, message={}", request.getRequestURI(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handleNotFound(NoHandlerFoundException e, HttpServletRequest request) {
        log.warn("[NoHandlerFound] path={}", request.getRequestURI());
        return Result.fail(HttpResultCode.NOT_FOUND);
    }

    // ======================== 409 用户冲突 ========================

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Result<Void> handleUserAlreadyExists(UserAlreadyExistsException e, HttpServletRequest request) {
        log.warn("[UserAlreadyExistsException] path={}, message={}", request.getRequestURI(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Result<Void> handleDataIntegrityViolation(DataIntegrityViolationException e, HttpServletRequest request) {
        log.warn("[DataIntegrityViolation] path={}, message={}", request.getRequestURI(), e.getMessage());
        return Result.fail(HttpResultCode.CONFLICT.getCode(), "数据冲突，请检查输入信息");
    }

}
