package com.microservice.user.service.service.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microservice.common.config.CacheConfig;
import com.microservice.common.constant.SecurityConstants;
import com.microservice.common.enums.VerifyCodeType;
import com.microservice.common.exception.user.UserAlreadyExistsException;
import com.microservice.common.exception.user.UserNotFoundException;
import com.microservice.common.exception.verifycode.VerificationCodeErrorException;
import com.microservice.common.exception.verifycode.VerificationCodeExpiredException;
import com.microservice.common.exception.verifycode.VerificationCodeSendFailedException;
import com.microservice.common.util.EmailUtils;
import com.microservice.common.util.EncryptUtils;
import com.microservice.common.util.VerifyCodeUtils;
import com.microservice.user.service.domain.dto.UserCreateDTO;
import com.microservice.user.service.domain.dto.UserUpdateDTO;
import com.microservice.user.service.domain.po.UserPO;
import com.microservice.user.service.domain.vo.UserVO;
import com.microservice.user.service.mapper.UserMapper;
import com.microservice.user.service.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户业务实现
 *
 * @author microservice
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements IUserService {

    private final CacheManager cacheManager;
    private final JavaMailSender javaMailSender;

    @Value("${app.mail.from}")
    private String mailFrom;

    @Override
    public UserVO getUserById(Long id) {
        UserPO user = this.getById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        // 枚举字段通过 @JsonValue 自动序列化为 code，无需手动转换
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    @Override
    public UserVO getUserByUsername(String username) {
        LambdaQueryWrapper<UserPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPO::getUsername, username);
        UserPO user = this.getOne(wrapper);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    @Override
    public List<UserVO> getUsersByIds(List<Long> ids) {
        List<UserPO> users = this.listByIds(ids);
        if (users == null || users.isEmpty()) {
            throw new UserNotFoundException();
        }
        return users.stream()
                .map(user -> BeanUtil.copyProperties(user, UserVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(Long id) {
        // 检查用户是否存在
        UserPO user = this.getById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        // 逻辑删除（MyBatis-Plus 已配置 logic-delete-field: deleted）
        this.removeById(id);
        log.info("用户已逻辑删除 -> id={}", id);
    }

    @Override
    public void deleteUsersByIds(List<Long> ids) {
        // 检查用户是否存在
        List<UserPO> users = this.listByIds(ids);
        if (users == null || users.isEmpty()) {
            throw new UserNotFoundException();
        }
        // 批量逻辑删除
        this.removeByIds(ids);
        log.info("用户已批量逻辑删除 -> ids={}, 实际删除数={}", ids, users.size());
    }

    @Override
    public void updateUser(UserUpdateDTO dto) {
        // 检查用户是否存在
        UserPO existingUser = this.getById(dto.getId());
        if (existingUser == null) {
            throw new UserNotFoundException();
        }
        // 构建更新对象，仅更新非空字段（MyBatis-Plus 已配置 update-strategy: not_null）
        UserPO updatePO = new UserPO();
        updatePO.setId(dto.getId());
        updatePO.setEmail(dto.getEmail());
        updatePO.setPhone(dto.getPhone());
        updatePO.setQq(dto.getQq());
        updatePO.setWechat(dto.getWechat());
        updatePO.setAvatar(dto.getAvatar());
        updatePO.setGender(dto.getGender());
        updatePO.setAge(dto.getAge());
        updatePO.setDescription(dto.getDescription());
        this.updateById(updatePO);
        log.info("用户信息已更新 -> id={}", dto.getId());
    }

    @Override
    public void register(UserCreateDTO dto) {
        // ====== 第一步：验证邮箱验证码 ======
        // 从 Redis 中获取注册验证码缓存，key 格式：verify_code::auth:register_verify_code:{email}
        Cache cache = cacheManager.getCache(CacheConfig.CACHE_VERIFY_CODE);
        if (cache == null) {
            throw new IllegalStateException("验证码缓存未初始化，请检查 CacheConfig 配置");
        }
        String cacheKey = SecurityConstants.REDIS_KEY_REGISTER_VERIFY_CODE + dto.getEmail();
        Cache.ValueWrapper wrapper = cache.get(cacheKey);

        // 验证码已过期（Redis 中不存在，说明超过 5 分钟 TTL）
        if (wrapper == null) {
            log.warn("验证码已过期 -> email={}", dto.getEmail());
            throw new VerificationCodeExpiredException();
        }
        // 验证码不匹配（用户输入的 code 与 Redis 中存储的不一致）
        if (!dto.getCode().equals(wrapper.get())) {
            log.warn("验证码错误 -> email={}, inputCode={}", dto.getEmail(), dto.getCode());
            throw new VerificationCodeErrorException();
        }

        // ====== 第二步：校验用户名唯一性 ======
        // 查询数据库中是否已存在相同用户名的用户（包含已逻辑删除的用户，防止注册后用户名被占用）
        long usernameCount = this.count(new LambdaQueryWrapper<UserPO>()
                .eq(UserPO::getUsername, dto.getUsername()));
        if (usernameCount > 0) {
            log.warn("用户名已存在 -> username={}", dto.getUsername());
            throw new UserAlreadyExistsException("用户名已存在");
        }

        // ====== 第三步：校验邮箱唯一性 ======
        long emailCount = this.count(new LambdaQueryWrapper<UserPO>()
                .eq(UserPO::getEmail, dto.getEmail()));
        if (emailCount > 0) {
            log.warn("邮箱已被注册 -> email={}", dto.getEmail());
            throw new UserAlreadyExistsException("邮箱已被注册");
        }

        // ====== 第四步：构建用户实体并写入数据库 ======
        // 密码使用 BCrypt 加密存储，不可逆
        UserPO user = new UserPO();
        user.setUsername(dto.getUsername());
        user.setPassword(EncryptUtils.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setQq(dto.getQq());
        user.setWechat(dto.getWechat());
        user.setAvatar(dto.getAvatar());
        user.setGender(dto.getGender());
        user.setAge(dto.getAge());
        // roleType、status、deleted 字段使用 UserPO 中的默认值，无需手动设置
        this.save(user);
        log.info("用户注册成功 -> id={}, username={}", user.getId(), user.getUsername());

        // ====== 第五步：清除已使用的验证码 ======
        // 注册成功后立即从 Redis 中删除验证码，防止验证码被重复使用
        cache.evict(cacheKey);
        log.info("注册验证码已清除 -> email={}", dto.getEmail());
    }

    @Override
    public String sendVerifyCode(String email, VerifyCodeType type) {
        // 获取验证码缓存实例（底层由 RedisCacheManager 管理，对应 Redis 中 "verify_code::" 前缀）
        Cache cache = cacheManager.getCache(CacheConfig.CACHE_VERIFY_CODE);
        if (cache == null) {
            throw new IllegalStateException("验证码缓存未初始化，请检查 CacheConfig 配置");
        }

        // 根据验证码业务类型选择对应的 Redis Key 前缀，保证登录/注册验证码互不干扰
        // 实际 Redis key 格式：verify_code::auth:{login|register}_verify_code:{email}
        String keyPrefix = switch (type) {
            case LOGIN -> SecurityConstants.REDIS_KEY_LOGIN_VERIFY_CODE;
            case REGISTER -> SecurityConstants.REDIS_KEY_REGISTER_VERIFY_CODE;
        };
        String cacheKey = keyPrefix + email;

        // ====== 第一步：检查该邮箱是否已存在未过期的验证码（防刷机制） ======
        // 如果缓存命中，说明用户在 5 分钟内已请求过验证码，返回 null 由上层处理
        Cache.ValueWrapper existingWrapper = cache.get(cacheKey);
        if (existingWrapper != null) {
            log.info("验证码冷却中，跳过重复发送 -> email={}, type={}", email, type);
            return null;
        }

        // ====== 第二步：生成 6 位随机验证码并存入缓存 ======
        // 使用 SecureRandom 生成验证码，确保随机性安全
        String code = VerifyCodeUtils.generateCode();
        // 将验证码存入 Spring Cache（底层写入 Redis），TTL 由 CacheConfig 统一配置（5 分钟）
        // put 操作是原子性的，缓存过期后自动清除，无需手动管理生命周期
        cache.put(cacheKey, code);
        log.info("验证码已存入缓存 -> email={}, type={}, cacheKey={}", email, type, cacheKey);

        // ====== 第三步：发送验证码邮件 ======
        try {
            EmailUtils.sendVerifyCode(javaMailSender, mailFrom, email, code, type);
            log.info("验证码邮件发送成功 -> email={}, type={}", email, type);
        } catch (Exception e) {
            // 邮件发送失败时，清除已存入的验证码，避免用户收到过期验证码后无法重新获取
            cache.evict(cacheKey);
            log.error("验证码邮件发送失败，已清除缓存 -> email={}, type={}", email, type, e);
            throw new VerificationCodeSendFailedException();
        }

        // ====== 第四步：返回生成的验证码 ======
        return code;
    }
}
