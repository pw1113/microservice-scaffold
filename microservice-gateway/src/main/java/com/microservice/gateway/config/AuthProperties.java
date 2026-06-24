package com.microservice.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证路径配置属性
 * <p>
 * 绑定 gateway.auth.* 配置项，
 * 定义需要认证的路径和排除认证的路径。
 * </p>
 *
 * @author microservice
 */
@Data
@Component
@ConfigurationProperties(prefix = "gateway.auth")
public class AuthProperties {

    /** 需要认证的路径（为空表示全部需要认证） */
    private List<String> includePaths = new ArrayList<>();

    /** 排除认证的路径（白名单） */
    private List<String> excludePaths = new ArrayList<>();
}
