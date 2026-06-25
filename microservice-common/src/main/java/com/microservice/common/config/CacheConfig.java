package com.microservice.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring Cache + Redis 缓存配置
 * <p>
 * 启用 Spring Cache 抽象层，底层使用 Redis 作为缓存存储。
 * 通过 {@link RedisCacheManager} 统一管理缓存实例，
 * 各业务模块可通过 {@code CacheManager.getCache(name)} 获取缓存操作对象。
 * </p>
 * <p>
 * <b>序列化策略：</b>Key 和 Value 均使用 {@link StringRedisSerializer}，
 * 适用于验证码等纯字符串场景，Redis 中存储格式直观可读。
 * </p>
 *
 * @author microservice
 */
@Configuration
@EnableCaching
@ConditionalOnClass(RedisConnectionFactory.class)
public class CacheConfig {

    /** 验证码缓存名称，对应 Redis 中的缓存前缀 */
    public static final String CACHE_VERIFY_CODE = "verify_code";

    /** 登录失败计数缓存名称，对应 Redis 中的缓存前缀 */
    public static final String CACHE_LOGIN_FAIL_COUNT = "login_fail_count";

    /** Token 黑名单缓存名称，对应 Redis 中的缓存前缀 */
    public static final String CACHE_TOKEN_BLACKLIST = "token_blacklist";

    /** RefreshToken 黑名单缓存名称，对应 Redis 中的缓存前缀 */
    public static final String CACHE_REFRESH_BLACKLIST = "refresh_blacklist";

    /** 验证码缓存默认过期时间（分钟），与业务层验证码有效期保持一致 */
    private static final long VERIFY_CODE_EXPIRE_MINUTES = 5;

    /** 登录失败计数缓存过期时间（分钟），与登录失败锁定时间保持一致 */
    private static final long LOGIN_FAIL_COUNT_EXPIRE_MINUTES = 15;

    /**
     * 配置 Redis 缓存管理器
     * <p>
     * 构建 {@link RedisCacheManager}，为不同缓存空间配置独立的 TTL。
     * 默认 TTL 为 5 分钟，可通过 {@code cacheConfigurations} 为特定缓存覆盖。
     * </p>
     *
     * @param connectionFactory Redis 连接工厂，由 Spring Boot 自动配置注入
     * @return RedisCacheManager 实例
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        // 使用 StringRedisSerializer 序列化 Key 和 Value（适用于字符串类型缓存）
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        // 默认缓存配置：所有缓存空间共享的基础配置
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                // 设置默认 TTL 为 5 分钟
                .entryTtl(Duration.ofMinutes(VERIFY_CODE_EXPIRE_MINUTES))
                // Key 序列化：使用 String 序列化器，Redis 中直接存储明文 key
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringSerializer))
                // Value 序列化：使用 String 序列化器，适用于验证码等字符串值
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(stringSerializer))
                // 不缓存 null 值，防止缓存穿透
                .disableCachingNullValues();

        // 各缓存空间的独立配置（可按需扩展）
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        // 验证码缓存：5 分钟过期，与验证码有效期一致
        cacheConfigurations.put(CACHE_VERIFY_CODE, defaultConfig);
        // 登录失败计数缓存：15 分钟过期，与登录失败锁定时间一致
        cacheConfigurations.put(CACHE_LOGIN_FAIL_COUNT,
                defaultConfig.entryTtl(Duration.ofMinutes(LOGIN_FAIL_COUNT_EXPIRE_MINUTES)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();
    }
}
