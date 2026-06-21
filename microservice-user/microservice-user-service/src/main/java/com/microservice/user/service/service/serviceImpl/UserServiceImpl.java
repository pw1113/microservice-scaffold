package com.microservice.user.service.service.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microservice.common.constant.SecurityConstants;
import com.microservice.common.exception.user.UserNotFoundException;
import com.microservice.common.exception.verifycode.VerificationCodeSendFailedException;
import com.microservice.common.util.EmailUtils;
import com.microservice.common.util.VerifyCodeUtils;
import com.microservice.user.service.domain.dto.UserUpdateDTO;
import com.microservice.user.service.domain.po.UserPO;
import com.microservice.user.service.domain.vo.UserVO;
import com.microservice.user.service.mapper.UserMapper;
import com.microservice.user.service.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
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

    private final RedisTemplate<String, Object> redisTemplate;
    private final JavaMailSender javaMailSender;

    @Value("${app.mail.from}")
    private String mailFrom;

    /** 验证码在 Redis 中的过期时间（分钟） */
    private static final long VERIFY_CODE_EXPIRE_MINUTES = 5;

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
    public void sendVerifyCode(String email) {
        // 生成6位验证码
        String code = VerifyCodeUtils.generateCode();
        // 存储到 Redis，key = auth:verify_code:{email}
        String redisKey = SecurityConstants.REDIS_KEY_VERIFY_CODE + email;
        redisTemplate.opsForValue().set(redisKey, code, Duration.ofMinutes(VERIFY_CODE_EXPIRE_MINUTES));
        log.info("验证码已存入Redis -> email={}, key={}", email, redisKey);
        // 发送验证码邮件
        try {
            EmailUtils.sendVerifyCode(javaMailSender, mailFrom, email, code);
        } catch (Exception e) {
            // 发送失败时清理 Redis 中的验证码
            redisTemplate.delete(redisKey);
            throw new VerificationCodeSendFailedException();
        }
    }
}
