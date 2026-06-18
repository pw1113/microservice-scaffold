package com.microservice.user.service.service.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microservice.common.enums.RoleTypeEnum;
import com.microservice.common.enums.UserStatusEnum;
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
        UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
        vo.setRoleTypeDesc(RoleTypeEnum.of(user.getRoleType()).getDescription());
        vo.setStatusDesc(UserStatusEnum.NORMAL.getCode().equals(user.getStatus())
                ? UserStatusEnum.NORMAL.getDescription()
                : UserStatusEnum.FROZEN.getDescription());
        vo.setGenderDesc(user.getGender() == null ? "未知" : (user.getGender() == 1 ? "男" : "女"));
        return vo;
    }
}
