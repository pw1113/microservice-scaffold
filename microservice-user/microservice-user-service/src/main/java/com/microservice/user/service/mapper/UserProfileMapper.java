package com.microservice.user.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microservice.user.service.domain.po.UserProfilePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户档案 Mapper
 *
 * @author microservice
 */
@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfilePO> {
}
