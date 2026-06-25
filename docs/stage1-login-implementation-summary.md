# 阶段 1：基础登录登出 — 实现总结

## 实现概述

本阶段完成了用户登录功能的核心实现，包括验证码校验、密码验证、账号状态检查、Token 生成、登录信息更新等完整流程。

## 实现时间

2026-06-24

## 实现范围

根据 SSO 实施计划书阶段 1 的第 1 项需求「补全 login() 方法」，完成了以下功能：

1. **登录失败次数限制**：连续登录失败 5 次后锁定账号 15 分钟
2. **邮箱验证码校验**：验证登录验证码的正确性和有效性
3. **用户查询与密码验证**：根据用户名查询用户，验证 BCrypt 密码
4. **账号状态检查**：检查账号是否被冻结或禁用
5. **Token 生成**：生成 AccessToken（15 分钟）和 RefreshToken（3 天）
6. **RefreshToken 持久化**：将 RefreshToken 写入数据库，支持多设备管理
7. **登录信息更新**：更新用户档案的登录 IP、时间、次数、在线状态
8. **登录成功清理**：清除登录失败计数和已使用的验证码

## 修改文件清单

### 新增文件（5 个）

| 文件 | 说明 |
|------|------|
| `microservice-common/.../exception/auth/LoginFailLimitExceededException.java` | 登录失败次数超限异常 |
| `microservice-common/.../result/AuthResultCode.java` | 认证相关状态码枚举 |
| `microservice-user/.../config/JwtProperties.java` | JWT 配置属性类 |
| `microservice-user/.../config/JwtConfig.java` | JWT 配置启用类 |
| `microservice-user/.../domain/dto/SendVerifyCodeDTO.java` | 发送验证码请求参数 DTO |

### 修改文件（10 个）

| 文件 | 修改内容 |
|------|----------|
| `microservice-user/.../controller/UserController.java` | 实现 login() 接口，获取客户端 IP |
| `microservice-user/.../service/IUserService.java` | 新增 login() 方法签名 |
| `microservice-user/.../service/serviceImpl/UserServiceImpl.java` | 实现 login() 完整业务逻辑 |
| `microservice-user/.../domain/dto/LoginDTO.java` | 已存在，包含登录所需字段 |
| `microservice-user/.../domain/vo/LoginVO.java` | 已存在，登录响应结构 |
| `microservice-user/.../enums/UserEnums.java` | Status 枚举新增 DISABLED(3, "禁用") |
| `microservice-user/.../resources/application.yaml` | 新增 JWT 配置项 |
| `microservice-common/.../constant/SecurityConstants.java` | 新增登录失败计数相关常量 |
| `microservice-common/.../config/CacheConfig.java` | 新增登录失败计数缓存配置 |
| `microservice-common/.../handler/AuthExceptionHandler.java` | 新增 LoginFailLimitExceededException 和 UserNotFoundException 处理 |

## login() 方法流程

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
│   - 验证码过期 → 抛出 VerificationCodeExpiredException │
│   - 验证码错误 → 抛出 VerificationCodeErrorException │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第三步：查询用户                                      │
│   - 按 username 查询 user 表                         │
│   - 用户不存在 → 增加失败计数，抛出 UserNotFoundException │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第四步：验证密码                                      │
│   - BCrypt.matches(明文密码, 数据库密文)              │
│   - 密码错误 → 增加失败计数，抛出 UserPasswordErrorException │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第五步：检查账号状态                                  │
│   - FROZEN → 抛出 UserAccountFrozenException        │
│   - DISABLED → 抛出 UserAccountDisabledException    │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第六步：生成 Token                                    │
│   - AccessToken: 15 分钟有效，包含 userId/username/roleType │
│   - RefreshToken: 3 天有效                           │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第七步：持久化 RefreshToken                           │
│   - 写入 user_refresh_token 表                       │
│   - 包含 userId、deviceId、过期时间                   │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第八步：更新用户登录信息                              │
│   - 查询或创建 user_profile 记录                     │
│   - 更新 lastLoginIp、lastLoginTime、loginCount、onlineStatus │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第九步：清除登录失败计数                              │
│   - 删除 Redis 中的 auth:login_fail:{username}       │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第十步：清除已使用的验证码                            │
│   - 删除 Redis 中的 auth:login_verify_code:{email}   │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│ 第十一步：返回登录结果                                │
│   - LoginVO: userId, username, accessToken,         │
│              refreshToken, expiresIn                │
└─────────────────────────────────────────────────────┘
```

## Redis Key 设计

| Key 格式 | 用途 | TTL |
|----------|------|-----|
| `auth:login_fail:{username}` | 登录失败计数 | 15 分钟 |
| `auth:login_verify_code:{email}` | 登录验证码 | 5 分钟 |
| `auth:token_blacklist:{token}` | AccessToken 黑名单 | Token 剩余有效期 |
| `auth:refresh_blacklist:{token}` | RefreshToken 黑名单 | Token 剩余有效期 |

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

### 异常处理器更新

**AuthExceptionHandler** 新增处理：
- `LoginFailLimitExceededException` → 429 Too Many Requests
- `UserNotFoundException` → 401 Unauthorized（登录场景下用户不存在返回 401）

**UserExceptionHandler** 已有处理：
- `UserNotFoundException` → 404 Not Found（查询场景下用户不存在返回 404）
- `UserAccountFrozenException` → 403 Forbidden
- `UserAccountDisabledException` → 403 Forbidden

## 配置项

### application.yaml 新增配置

```yaml
# JWT 配置（需与网关保持一致）
jwt:
  secret: bWljcm9zZXJ2aWNlLXNlY3JldC1rZXktZm9yLWp3dC1zaWduaW5nLTIwMjQ=
  access-token-ttl: 15m
  refresh-token-ttl: 3d
```

## 本次优化（2026-06-24）

### 1. 简化 UserCreateDTO

**修改内容**：
- 仅保留 `username`、`password`、`email`、`code` 四个字段
- 移除 `phone`、`qq`、`wechat`、`avatar`、`gender`、`age` 等可选字段
- 注册时只需提供核心信息，其他信息通过"修改用户信息"接口补充

**影响范围**：
- `UserCreateDTO.java` — 简化字段
- `UserServiceImpl.register()` — 移除对已删除字段的引用

### 2. 注册验证码邮箱唯一性校验

**修改内容**：
- 发送注册验证码时，先检查邮箱是否已注册
- 已注册的邮箱拒绝发送验证码，抛出 `UserAlreadyExistsException`
- 将邮箱唯一性校验前置到验证码发送阶段，提升用户体验

**影响范围**：
- `UserServiceImpl.sendVerifyCode()` — 新增邮箱唯一性校验
- `UserServiceImpl.register()` — 移除邮箱唯一性校验（已在发送验证码时验证）

### 3. UserPO 字段默认值

**修改内容**：
- `avatar` — 默认值 `"https://cdn.example.com/default-avatar.png"`
- `gender` — 默认值 `UserEnums.Gender.UNKNOWN`
- `roleType` — 默认值 `UserEnums.RoleType.USER`
- `status` — 默认值 `UserEnums.Status.NORMAL`
- `deleted` — 默认值 `UserEnums.Deleted.NOT_DELETED`
- `phone`、`qq`、`wechat`、`age`、`description` — 允许为空

## 待完成事项

- [ ] logout() 方法实现
- [ ] token 黑名单写入逻辑
- [ ] 前端联调测试

## 验证方式

1. 启动 user 服务
2. 调用 `POST /user/send-code` 发送登录验证码
3. 调用 `POST /user/login` 传入用户名、密码、邮箱、验证码
4. 验证返回的 accessToken 和 refreshToken
5. 使用 accessToken 请求受保护接口

## 验证标准完成情况

| 验证项 | 状态 | 说明 |
|--------|------|------|
| POST /user/login 返回 accessToken + refreshToken | ✅ | LoginVO 包含 userId、username、accessToken、refreshToken、expiresIn |
| 带 accessToken 请求受保护接口，网关放行并传递用户信息 | ⏳ | 网关层已实现转发，待联调验证 |
| 不带 token 请求受保护接口，网关返回 401 | ⏳ | 网关层已实现，待联调验证 |
| POST /user/logout 后，原 accessToken 失效 | 🔲 | 待实现 logout() 方法 |
| refreshToken 能正确持久化到数据库 | ✅ | 写入 user_refresh_token 表 |
