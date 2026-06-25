package com.microservice.user.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microservice.user.service.domain.dto.LoginDTO;
import com.microservice.user.service.domain.dto.SendVerifyCodeDTO;
import com.microservice.user.service.domain.dto.UserCreateDTO;
import com.microservice.user.service.domain.dto.UserUpdateDTO;
import com.microservice.user.service.domain.po.UserPO;
import com.microservice.user.service.domain.vo.LoginVO;
import com.microservice.user.service.domain.vo.UserVO;

import java.util.List;

/**
 * 用户业务接口
 *
 * @author microservice
 */
public interface IUserService extends IService<UserPO> {

    /**
     * 根据 ID 查询用户
     *
     * @param id 用户ID
     * @return 用户视图对象
     */
    UserVO getUserById(Long id);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户视图对象
     */
    UserVO getUserByUsername(String username);

    /**
     * 根据 ID 集合批量查询用户
     *
     * @param ids 用户ID集合
     * @return 用户视图对象列表
     */
    List<UserVO> getUsersByIds(List<Long> ids);

    /**
     * 根据 ID 删除用户（逻辑删除）
     *
     * @param id 用户ID
     */
    void deleteUserById(Long id);

    /**
     * 根据 ID 集合批量删除用户（逻辑删除）
     *
     * @param ids 用户ID集合
     */
    void deleteUsersByIds(List<Long> ids);

    /**
     * 修改用户信息
     *
     * @param dto 用户更新DTO
     */
    void updateUser(UserUpdateDTO dto);

    /**
     * 注册新用户
     * <p>
     * 完整注册流程：验证邮箱验证码 → 校验用户名唯一性 → 密码 BCrypt 加密 → 写入数据库 → 清除已使用的验证码。
     * 注意：邮箱唯一性已在发送注册验证码时校验，此处不再重复校验。
     * </p>
     *
     * @param dto 用户注册信息（包含用户名、密码、邮箱、验证码）
     */
    void register(UserCreateDTO dto);

    /**
     * 发送邮箱验证码
     * <p>
     * 生成6位随机验证码，存入Redis缓存（5分钟过期），并通过邮件发送给用户。
     * 同一邮箱在同一业务类型下，验证码有效期内不可重复获取。
     * </p>
     *
     * @param dto 包含目标邮箱和验证码业务类型（登录/注册）
     * @return 生成的6位验证码
     */
    String sendVerifyCode(SendVerifyCodeDTO dto);

    /**
     * 用户登录
     * <p>
     * 完整登录流程：验证登录失败次数限制 → 验证邮箱验证码 → 验证密码（BCrypt） → 检查账号状态 →
     * 生成 accessToken + refreshToken → 持久化 refreshToken → 更新登录信息 → 清除失败计数。
     * </p>
     *
     * @param loginDTO 登录请求参数（用户名、密码、邮箱、验证码、设备标识）
     * @param clientIp 客户端IP，用于记录登录信息
     * @return 登录响应结果（userId、username、accessToken、refreshToken、expiresIn）
     */
    LoginVO login(LoginDTO loginDTO, String clientIp);

    /**
     * 用户登出
     * <p>
     * 完整登出流程：将 accessToken 加入 Redis 黑名单 → 将 refreshToken 加入 Redis 黑名单 →
     * 更新用户在线状态为 OFFLINE。黑名单 Token 的过期时间等于 Token 剩余有效期，避免 Redis 无限膨胀。
     * </p>
     *
     * @param accessToken  访问令牌
     * @param refreshToken 刷新令牌
     * @param userId       用户ID
     */
    void logout(String accessToken, String refreshToken, Long userId);
}
