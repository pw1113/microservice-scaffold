package com.microservice.user.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microservice.common.enums.VerifyCodeType;
import com.microservice.user.service.domain.dto.UserUpdateDTO;
import com.microservice.user.service.domain.po.UserPO;
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
     * 发送邮箱验证码
     * <p>
     * 生成6位随机验证码，存入Redis缓存（5分钟过期），并通过邮件发送给用户。
     * 同一邮箱在同一业务类型下，验证码有效期内不可重复获取。
     * </p>
     *
     * @param email 目标邮箱
     * @param type  验证码业务类型（登录/注册），不同类型的验证码使用独立的 Redis Key，互不干扰
     * @return 生成的6位验证码
     */
    String sendVerifyCode(String email, VerifyCodeType type);
}
