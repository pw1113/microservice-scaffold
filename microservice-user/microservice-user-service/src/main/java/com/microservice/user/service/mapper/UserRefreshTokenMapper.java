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
}
