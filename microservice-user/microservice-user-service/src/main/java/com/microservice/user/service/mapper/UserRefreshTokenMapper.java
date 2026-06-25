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
}
