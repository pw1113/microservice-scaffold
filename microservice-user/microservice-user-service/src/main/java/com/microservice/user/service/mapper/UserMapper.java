package com.microservice.user.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microservice.user.service.domain.po.UserPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 *
 * @author microservice
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPO> {

}
