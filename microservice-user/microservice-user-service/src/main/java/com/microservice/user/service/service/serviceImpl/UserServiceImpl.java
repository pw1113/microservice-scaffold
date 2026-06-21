package com.microservice.user.service.service.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microservice.common.exception.user.UserNotFoundException;
import com.microservice.user.service.domain.po.UserPO;
import com.microservice.user.service.domain.vo.UserVO;
import com.microservice.user.service.mapper.UserMapper;
import com.microservice.user.service.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户业务实现
 *
 * @author microservice
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements IUserService {

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
}
