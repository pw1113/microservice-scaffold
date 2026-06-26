package com.microservice.user.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microservice.user.service.domain.po.UserRefreshTokenPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户刷新令牌 Mapper
 *
 * @author microservice
 */
@Mapper
public interface UserRefreshTokenMapper extends BaseMapper<UserRefreshTokenPO> {

    /**
     * 插入或更新 RefreshToken（Upsert 操作）
     * <p>
     * 如果 user_id + device_id 不存在，则插入新记录；
     * 如果 user_id + device_id 已存在，则更新 refresh_token 和 expire_time。
     * 使用 MySQL 的 INSERT ... ON DUPLICATE KEY UPDATE 语法，单次数据库操作完成。
     * </p>
     *
     * @param refreshTokenPO RefreshToken 持久化对象
     * @return 影响行数
     */
    int upsert(UserRefreshTokenPO refreshTokenPO);

    /**
     * 根据 refreshToken 查询记录
     *
     * @param refreshToken 刷新令牌
     * @return 匹配的记录，不存在返回 null
     */
    UserRefreshTokenPO selectByRefreshToken(String refreshToken);

    /**
     * 根据 userId + deviceId 查询记录
     *
     * @param userId   用户 ID
     * @param deviceId 设备标识
     * @return 匹配的记录，不存在返回 null
     */
    UserRefreshTokenPO selectByUserIdAndDeviceId(Long userId, String deviceId);

    /**
     * 根据 userId + deviceId 删除记录（踢掉指定设备）
     *
     * @param userId   用户 ID
     * @param deviceId 设备标识
     * @return 影响行数
     */
    int deleteByUserIdAndDeviceId(Long userId, String deviceId);

    /**
     * 根据 userId 删除所有记录（踢掉所有设备）
     *
     * @param userId 用户 ID
     * @return 影响行数
     */
    int deleteByUserId(Long userId);
}
