package com.microservice.user.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microservice.user.service.domain.po.UserPO;
import com.microservice.user.service.domain.vo.UserVO;

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
}
