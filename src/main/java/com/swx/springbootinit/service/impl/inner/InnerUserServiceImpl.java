package com.swx.springbootinit.service.impl.inner;

import com.alibaba.nacos.shaded.io.grpc.stub.annotations.RpcMethod;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.swx.apicommon.model.entity.User;
import com.swx.apicommon.service.InnerUserService;
import com.swx.springbootinit.common.ErrorCode;
import com.swx.springbootinit.exception.BusinessException;
import com.swx.springbootinit.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
//@RpcService(interfaceType = InnerUserService.class)
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
