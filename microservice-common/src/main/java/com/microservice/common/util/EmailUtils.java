package com.microservice.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 邮件发送工具类
 * <p>
 * 纯静态工具，不注册为 Spring Bean，由调用方传入 JavaMailSender 实例。
 * 需要在 application.yaml 中配置 spring.mail 相关属性。
 * </p>
 *
 * @author microservice
 */
@Slf4j
public final class EmailUtils {

    private EmailUtils() {
    }

    /**
     * 发送验证码邮件
     *
     * @param mailSender  邮件发送器（由调用方注入并传入）
     * @param from        发件人邮箱（需与 spring.mail.username 一致）
     * @param to          收件人邮箱
     * @param code        验证码
     */
    public static void sendVerifyCode(JavaMailSender mailSender, String from, String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("【Microservice Scaffold】邮箱验证码");
        message.setText("您的验证码为：" + code + "，有效期5分钟，请勿泄露给他人。");
        message.setFrom(from);
        message.setTo(to);
        try {
            mailSender.send(message);
            log.info("验证码邮件已发送 -> from={}, to={}", from, to);
        } catch (Exception e) {
            log.error("验证码邮件发送失败 -> from={}, to={}", from, to, e);
            throw new RuntimeException("邮件发送失败", e);
        }
    }
}
