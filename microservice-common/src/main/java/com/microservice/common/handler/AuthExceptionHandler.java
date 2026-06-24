package com.microservice.common.handler;

import com.microservice.common.exception.BusinessException;
import com.microservice.common.exception.UnauthorizedException;
import com.microservice.common.exception.token.RefreshTokenExpiredException;
import com.microservice.common.exception.token.RefreshTokenInvalidException;
import com.microservice.common.exception.token.TokenBlacklistedException;
import com.microservice.common.exception.token.TokenExpiredException;
import com.microservice.common.exception.token.TokenInvalidException;
import com.microservice.common.exception.user.UserNotLoginException;
import com.microservice.common.exception.user.UserPasswordErrorException;
import com.microservice.common.exception.verifycode.VerificationCodeCooldownException;
import com.microservice.common.exception.verifycode.VerificationCodeErrorException;
import com.microservice.common.exception.verifycode.VerificationCodeExpiredException;
import com.microservice.common.exception.verifycode.VerificationCodeSendFailedException;
import com.microservice.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 认证 / Token / 验证码 异常处理器
 *
 * @author microservice
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class AuthExceptionHandler {

    // ======================== 401 认证失败 ========================

    @ExceptionHandler({
            TokenExpiredException.class,
            TokenInvalidException.class,
            TokenBlacklistedException.class,
            RefreshTokenExpiredException.class,
            RefreshTokenInvalidException.class,
            UserNotLoginException.class,
            UserPasswordErrorException.class,
            UnauthorizedException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleUnauthorized(BusinessException e, HttpServletRequest request) {
        log.warn("[{}] path={}, code={}, message={}", e.getClass().getSimpleName(),
                request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    // ======================== 400 验证码错误 ========================

    @ExceptionHandler({
            VerificationCodeErrorException.class,
            VerificationCodeExpiredException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleVerifyCodeError(BusinessException e, HttpServletRequest request) {
        log.warn("[{}] path={}, code={}, message={}", e.getClass().getSimpleName(),
                request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    // ======================== 429 验证码冷却 ========================

    @ExceptionHandler(VerificationCodeCooldownException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public Result<Void> handleCooldown(VerificationCodeCooldownException e, HttpServletRequest request) {
        log.warn("[VerificationCodeCooldown] path={}, message={}", request.getRequestURI(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    // ======================== 500 验证码发送失败 ========================

    @ExceptionHandler(VerificationCodeSendFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleVerifyCodeSendFailed(VerificationCodeSendFailedException e, HttpServletRequest request) {
        log.error("[VerificationCodeSendFailed] path={}, message={}", request.getRequestURI(), e.getMessage(), e);
        return Result.fail(e.getCode(), e.getMessage());
    }

}
