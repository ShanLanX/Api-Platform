package com.yupi.springbootinit.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.swx.apicommon.model.entity.User;
import com.swx.apicommon.service.InnerUserService;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.annotation.Resources;
@DubboService
public class InnerUserServiceImpl implements InnerUserService {
    @Resource
    UserMapper userMapper;
    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> q=new QueryWrapper<>();
        q.eq("accessKey",accessKey);
        User user=userMapper.selectOne(q);
        return user;


    }
}
