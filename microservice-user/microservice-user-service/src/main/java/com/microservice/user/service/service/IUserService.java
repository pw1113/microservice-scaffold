package com.microservice.user.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
}
