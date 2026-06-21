package com.microservice.user.service.service.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microservice.common.exception.BusinessException;
import com.microservice.common.result.ResultCode;
import com.microservice.user.service.domain.po.UserPO;
import com.microservice.user.service.domain.vo.UserVO;
import com.microservice.user.service.mapper.UserMapper;
import com.microservice.user.service.service.IUserService;
import org.springframework.stereotype.Service;

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
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        // 枚举字段通过 @JsonValue 自动序列化为 code，无需手动转换
        return BeanUtil.copyProperties(user, UserVO.class);
    }
}
