# 阶段 1：基础登录登出 — Logout 实现总结

## 实现概述

本阶段完成了用户登出功能的核心实现，包括 Token 黑名单机制、用户在线状态更新等完整流程。

## 实现时间

2026-06-25

## 实现范围

根据 SSO 实施计划书阶段 1 的第 2 项需求「新增 logout() 方法」，完成了以下功能：

1. **Token 黑名单机制**：将 accessToken 和 refreshToken 加入 Redis 黑名单
2. **动态 TTL 策略**：黑名单 Token 的过期时间等于 Token 剩余有效期，避免 Redis 无限膨胀
3. **用户在线状态更新**：登出时将 user_profile 的 onlineStatus 更新为 OFFLINE
4. **Token 有效期计算**：解析 JWT 获取过期时间，计算剩余有效期

## 修改文件清单

### 新增文件（1 个）

| 文件 | 说明 |
|------|------|
| `microservice-user/.../domain/dto/LogoutDTO.java` | 用户登出请求参数 DTO |

### 修改文件（4 个）

| 文件 | 修改内容 |
|------|----------|
| `microservice-user/.../service/IUserService.java` | 新增 `logout()` 方法签名 |
| `microservice-user/.../service/serviceImpl/UserServiceImpl.java` | 实现 `logout()` 方法和 `calculateTokenRemainingTtl()` 辅助方法 |
| `microservice-user/.../controller/UserController.java` | 新增 `POST /user/logout` 接口，使用 `LogoutDTO` 封装参数 |
| `microservice-common/.../config/CacheConfig.java` | 新增 Token 黑名单缓存名称常量 |

## logout() 方法流程

```
POST /user/logout
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第一步：将 accessToken 加入 Redis 黑名单              │
│   - Redis key: auth:token_blacklist:{token}         │
│   - TTL: token 剩余有效期（秒）                      │
│   - 解析 JWT 获取过期时间，计算剩余 TTL               │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第二步：将 refreshToken 加入 Redis 黑名单             │
│   - Redis key: auth:refresh_blacklist:{token}       │
│   - TTL: token 剩余有效期（秒）                      │
│   - 与 accessToken 相同的黑名单策略                   │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第三步：更新用户在线状态为 OFFLINE                    │
│   - 查询 user_profile 表                            │
│   - 更新 onlineStatus 为 OFFLINE                    │
│   - 如果 user_profile 不存在，跳过此步骤             │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 登出完成                                            │
│   - 返回成功响应                                    │
│   - 原 accessToken 和 refreshToken 立即失效         │
└─────────────────────────────────────────────────────┘
```

## API 接口设计

### POST /user/logout

**请求头：**
```
X-User-Id: {userId}  // 由网关注入
```

**请求体（JSON）：**
```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| accessToken | String | 是 | 访问令牌 |
| refreshToken | String | 是 | 刷新令牌 |

**响应示例：**
```json
{
    "code": 200,
    "message": "登出成功",
    "data": null
}
```

**设计说明：**
- 使用 `LogoutDTO` 封装请求参数，符合 RESTful 规范
- `userId` 从网关注入的 `X-User-Id` 头部获取，与网关解耦
- 不依赖 `Authorization` 头部，避免网关移除该头部时出现问题

## Redis Key 设计

| Key 格式 | 用途 | TTL |
|----------|------|-----|
| `auth:token_blacklist:{token}` | AccessToken 黑名单 | Token 剩余有效期 |
| `auth:refresh_blacklist:{token}` | RefreshToken 黑名单 | Token 剩余有效期 |

## 核心实现细节

### 1. Token 黑名单机制

```java
// 将 token 加入 Redis 黑名单，TTL 为 token 剩余有效期
String blacklistKey = SecurityConstants.REDIS_KEY_TOKEN_BLACKLIST + token;
redisTemplate.opsForValue().set(blacklistKey, "1", remainingTtl, TimeUnit.SECONDS);
```

**设计要点：**
- 使用 `RedisTemplate` 而非 `CacheManager`，因为需要动态 TTL
- 黑名单 value 为 `"1"`，仅用于判断是否存在
- TTL 等于 token 剩余有效期，过期后自动清除，避免 Redis 无限膨胀

### 2. Token 剩余有效期计算

```java
private long calculateTokenRemainingTtl(String token) {
    Claims claims = JwtUtils.parseToken(token, jwtProperties.getSecret());
    Date expiration = claims.getExpiration();
    long remainingMs = expiration.getTime() - System.currentTimeMillis();
    return remainingMs > 0 ? remainingMs / 1000 : -1;
}
```

**设计要点：**
- 解析 JWT 获取 `expiration` claim
- 计算当前时间与过期时间的差值
- 返回秒数，如果 token 已过期或解析失败返回 -1

### 3. 异常处理

- Token 解析失败时记录警告日志，跳过黑名单写入
- user_profile 不存在时跳过在线状态更新
- 不影响登出主流程的正常返回

## 依赖关系

### 依赖的已有组件

| 组件 | 用途 |
|------|------|
| `JwtUtils.parseToken()` | 解析 JWT Token |
| `JwtProperties` | 获取 JWT 密钥 |
| `RedisTemplate` | 操作 Redis 设置黑名单 |
| `UserProfileMapper` | 查询和更新用户档案 |
| `SecurityConstants` | Redis Key 前缀常量 |

### 新增的依赖

| 依赖 | 用途 |
|------|------|
| `io.jsonwebtoken.Claims` | JWT Claims 解析 |
| `java.util.concurrent.TimeUnit` | 时间单位转换 |

## 验证标准

| 验证项 | 状态 | 说明 |
|--------|------|------|
| POST /user/logout 返回成功 | ✅ | 返回 200 和成功消息 |
| AccessToken 加入黑名单 | ✅ | 写入 Redis，TTL 为剩余有效期 |
| RefreshToken 加入黑名单 | ✅ | 写入 Redis，TTL 为剩余有效期 |
| 用户在线状态更新为 OFFLINE | ✅ | 更新 user_profile 表 |
| Token 黑名单自动过期 | ✅ | TTL 等于 token 剩余有效期 |

## 与网关 AuthFilter 的配合

网关 `AuthGlobalFilter` 已集成 Token 黑名单检查：

```java
// 检查 Token 是否在黑名单中
String blacklistKey = SecurityConstants.REDIS_KEY_TOKEN_BLACKLIST + token;
return reactiveRedisTemplate.hasKey(blacklistKey)
        .flatMap(isBlacklisted -> {
            if (isBlacklisted) {
                log.warn("[Gateway] Token 已在黑名单中: {} {}", method, path);
                return unauthorized(exchange, "Token 已失效，请重新登录");
            }
            // 继续处理请求...
        });
```

**实现要点**：
- 使用 `ReactiveRedisTemplate`（响应式 Redis 客户端）
- 在 Token 解析成功后、提取用户信息前检查黑名单
- 黑名单命中时返回 401，提示"Token 已失效，请重新登录"

## 后续优化建议

1. **批量登出**：支持"踢掉所有设备"功能，按 userId 批量使所有 token 失效
2. **Token 版本校验**：结合 `tokenVersion` 机制，实现更细粒度的 token 失效控制
3. **登出事件发布**：登出时发布事件，供其他服务监听处理

## 待完成事项

- [x] 网关 AuthFilter 集成黑名单检查
- [ ] 前端联调测试
- [ ] 多设备登出支持
