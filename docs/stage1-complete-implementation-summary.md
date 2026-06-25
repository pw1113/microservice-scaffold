# 阶段 1：基础登录登出 — 完整实现总结

## 实现概述

本阶段完成了用户登录登出功能的完整实现，包括验证码校验、密码验证、Token 生成、Token 黑名单、用户在线状态管理等核心功能。

## 实现时间

- 登录功能：2026-06-24
- 登出功能：2026-06-25
- 网关黑名单检查：2026-06-25

## 实现范围

根据 SSO 实施计划书阶段 1 的全部需求，完成了以下功能：

### 1. 补全 login() 方法 ✅

- 登录失败次数限制（5 次锁定 15 分钟）
- 邮箱验证码校验
- 用户查询与密码验证（BCrypt）
- 账号状态检查（冻结/禁用）
- Token 生成（AccessToken 15 分钟 + RefreshToken 3 天）
- RefreshToken 持久化到数据库
- 登录信息更新（IP、时间、次数、在线状态）
- 登录成功清理（失败计数、验证码）

### 2. 新增 logout() 方法 ✅

- 使用 LogoutDTO 封装 accessToken 和 refreshToken
- userId 从网关注入的 X-User-Id 头部获取
- 将 accessToken 加入 Redis 黑名单（TTL = 剩余有效期）
- 将 refreshToken 加入 Redis 黑名单（TTL = 剩余有效期）
- 更新 user_profile 的 onlineStatus 为 OFFLINE

### 3. 补全 Token 黑名单写入逻辑 ✅

- logout 时将 Token 写入 Redis 黑名单
- 网关 AuthFilter 检查 Token 黑名单
- 黑名单 TTL 等于 Token 剩余有效期，自动过期

### 4. 补全 UserRefreshTokenMapper ✅

- UserRefreshTokenPO 已定义
- 支持按 refreshToken 查询、按 userId+deviceId 查询、插入、删除

---

## 修改文件清单

### 新增文件（8 个）

| 文件 | 说明 |
|------|------|
| `microservice-common/.../exception/auth/LoginFailLimitExceededException.java` | 登录失败次数超限异常 |
| `microservice-common/.../result/AuthResultCode.java` | 认证相关状态码枚举 |
| `microservice-user/.../config/JwtProperties.java` | JWT 配置属性类 |
| `microservice-user/.../config/JwtConfig.java` | JWT 配置启用类 |
| `microservice-user/.../domain/dto/SendVerifyCodeDTO.java` | 发送验证码请求参数 DTO |
| `microservice-user/.../domain/dto/LogoutDTO.java` | 用户登出请求参数 DTO |

### 修改文件（12 个）

| 文件 | 修改内容 |
|------|----------|
| `microservice-user/.../controller/UserController.java` | 新增 login/logout 接口 |
| `microservice-user/.../service/IUserService.java` | 新增 login/logout 方法签名 |
| `microservice-user/.../service/serviceImpl/UserServiceImpl.java` | 实现 login/logout 完整业务逻辑 |
| `microservice-user/.../enums/UserEnums.java` | Status 枚举新增 DISABLED(3, "禁用") |
| `microservice-user/.../resources/application.yaml` | 新增 JWT 配置项 |
| `microservice-common/.../constant/SecurityConstants.java` | 新增登录失败计数、Token 黑名单相关常量 |
| `microservice-common/.../config/CacheConfig.java` | 新增登录失败计数、Token 黑名单缓存配置 |
| `microservice-common/.../handler/AuthExceptionHandler.java` | 新增异常处理 |
| `microservice-gateway/.../filter/AuthGlobalFilter.java` | 集成 Token 黑名单检查 |

---

## API 接口清单

### POST /user/login

**功能**：用户登录

**请求体**：
```json
{
    "username": "admin",
    "password": "123456",
    "email": "admin@example.com",
    "code": "123456",
    "deviceId": "device-001"
}
```

**响应**：
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "userId": 1,
        "username": "admin",
        "accessToken": "eyJhbGciOiJIUzI1NiIs...",
        "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
        "expiresIn": 900
    }
}
```

### POST /user/logout

**功能**：用户登出

**请求头**：
```
X-User-Id: 1  (网关注入)
```

**请求体**：
```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

**响应**：
```json
{
    "code": 200,
    "message": "登出成功",
    "data": null
}
```

### POST /user/send-code

**功能**：发送邮箱验证码

**请求体**：
```json
{
    "email": "admin@example.com",
    "type": "LOGIN"
}
```

---

## Redis Key 设计

| Key 格式 | 用途 | TTL |
|----------|------|-----|
| `auth:login_fail:{username}` | 登录失败计数 | 15 分钟 |
| `auth:login_verify_code:{email}` | 登录验证码 | 5 分钟 |
| `auth:register_verify_code:{email}` | 注册验证码 | 5 分钟 |
| `auth:token_blacklist:{token}` | AccessToken 黑名单 | Token 剩余有效期 |
| `auth:refresh_blacklist:{token}` | RefreshToken 黑名单 | Token 剩余有效期 |

---

## 核心流程图

### 登录流程

```
POST /user/login
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第一步：检查登录失败次数限制                          │
│   - Redis key: auth:login_fail:{username}           │
│   - 失败次数 >= 5 → 抛出 LoginFailLimitExceededException │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第二步：验证邮箱验证码                                │
│   - Redis key: auth:login_verify_code:{email}       │
│   - 验证码过期/错误 → 抛出异常                        │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第三步：查询用户 & 验证密码                           │
│   - 按 username 查询 user 表                         │
│   - BCrypt.matches(明文密码, 数据库密文)              │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第四步：检查账号状态                                  │
│   - FROZEN → 抛出 UserAccountFrozenException        │
│   - DISABLED → 抛出 UserAccountDisabledException    │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第五步：生成 Token                                    │
│   - AccessToken: 15 分钟有效                         │
│   - RefreshToken: 3 天有效                           │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第六步：持久化 RefreshToken & 更新登录信息            │
│   - 写入 user_refresh_token 表                       │
│   - 更新 user_profile（IP、时间、次数、在线状态）     │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第七步：清理 & 返回结果                               │
│   - 清除登录失败计数                                  │
│   - 清除已使用的验证码                                │
│   - 返回 LoginVO                                     │
└─────────────────────────────────────────────────────┘
```

### 登出流程

```
POST /user/logout
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第一步：将 accessToken 加入 Redis 黑名单              │
│   - Redis key: auth:token_blacklist:{token}         │
│   - TTL: token 剩余有效期                            │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第二步：将 refreshToken 加入 Redis 黑名单             │
│   - Redis key: auth:refresh_blacklist:{token}       │
│   - TTL: token 剩余有效期                            │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第三步：更新用户在线状态为 OFFLINE                    │
│   - 更新 user_profile.onlineStatus                  │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 登出完成                                            │
│   - 返回成功响应                                    │
│   - 原 Token 立即失效                               │
└─────────────────────────────────────────────────────┘
```

### 网关 Token 校验流程

```
请求到达网关
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第一步：检查白名单                                    │
│   - 白名单路径直接放行                                │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第二步：提取 Token                                    │
│   - 从 Authorization: Bearer {token} 提取           │
│   - 无 Token 返回 401                                │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第三步：解析 Token                                    │
│   - JwtUtils.parseToken() 解析 JWT                  │
│   - 解析失败返回 401                                  │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第四步：检查黑名单                                    │
│   - 查询 Redis: auth:token_blacklist:{token}        │
│   - 在黑名单中返回 401                               │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第五步：透传用户信息 & 放行                           │
│   - 注入 X-User-Id、X-User-Name、X-User-Role        │
│   - 转发请求到下游服务                                │
└─────────────────────────────────────────────────────┘
```

---

## 异常处理

| 异常类 | HTTP 状态码 | 错误码 | 触发场景 |
|--------|-------------|--------|----------|
| LoginFailLimitExceededException | 429 | 2001 | 登录失败 5 次 |
| VerificationCodeExpiredException | 400 | 3002 | 验证码过期 |
| VerificationCodeErrorException | 400 | 3001 | 验证码错误 |
| UserNotFoundException | 401 | 1001 | 用户不存在 |
| UserPasswordErrorException | 401 | 1002 | 密码错误 |
| UserAccountFrozenException | 403 | 1003 | 账号冻结 |
| UserAccountDisabledException | 403 | 1004 | 账号禁用 |

---

## 配置项

### application.yaml

```yaml
# JWT 配置（需与网关保持一致）
jwt:
  secret: bWljcm9zZXJ2aWNlLXNlY3JldC1rZXktZm9yLWp3dC1zaWduaW5nLTIwMjQ=
  access-token-ttl: 15m
  refresh-token-ttl: 3d
```

---

## 验证标准完成情况

| 验证项 | 状态 | 说明 |
|--------|------|------|
| POST /user/login 返回 accessToken + refreshToken | ✅ | LoginVO 包含完整登录信息 |
| 带 accessToken 请求受保护接口，网关放行并传递用户信息 | ✅ | 网关注入 X-User-Id 等头部 |
| 不带 token 请求受保护接口，网关返回 401 | ✅ | 网关 AuthFilter 拦截 |
| POST /user/logout 后，原 accessToken 失效 | ✅ | Redis 黑名单拦截 |
| refreshToken 能正确持久化到数据库 | ✅ | 写入 user_refresh_token 表 |
| 网关 AuthFilter 检查 Token 黑名单 | ✅ | 使用 ReactiveRedisTemplate 检查 |

---

## 与前端配合

### 前端登录流程

```javascript
// 1. 调用登录接口
const response = await fetch('/api/user/login', {
    method: 'POST',
    body: JSON.stringify({ username, password, email, code })
});
const { accessToken, refreshToken } = response.data;

// 2. 保存 Token
localStorage.setItem('accessToken', accessToken);
localStorage.setItem('refreshToken', refreshToken);
```

### 前端请求拦截器

```javascript
// 请求时自动携带 AccessToken
axios.interceptors.request.use(config => {
    const token = localStorage.getItem('accessToken');
    if (token) {
        config.headers['Authorization'] = 'Bearer ' + token;
    }
    return config;
});

// 响应时处理 401（Token 过期）
axios.interceptors.response.use(
    response => response,
    async error => {
        if (error.response.status === 401) {
            // 用 RefreshToken 续期
            const refreshToken = localStorage.getItem('refreshToken');
            const res = await axios.post('/api/user/refresh', { refreshToken });
            localStorage.setItem('accessToken', res.data.accessToken);
            // 重试原请求
            return axios(error.config);
        }
        return Promise.reject(error);
    }
);
```

### 前端登出流程

```javascript
// 调用登出接口
await fetch('/api/user/logout', {
    method: 'POST',
    headers: { 'Authorization': 'Bearer ' + accessToken },
    body: JSON.stringify({ accessToken, refreshToken })
});

// 清除本地 Token
localStorage.removeItem('accessToken');
localStorage.removeItem('refreshToken');

// 跳转登录页
window.location.href = '/login';
```

---

## 后续阶段

- **阶段 2：Token 刷新机制** — 实现 /user/refresh 接口，支持 AccessToken 无感续期
- **阶段 3：抽取独立 Auth 服务** — 认证逻辑与用户服务解耦
- **阶段 4：多系统 SSO** — 多个子系统共享登录态
