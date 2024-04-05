package com.swx.springbootinit.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.apicommon.model.entity.BlackList;
import com.swx.apicommon.service.BlackListService;
import com.swx.springbootinit.mapper.BlackListMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

//@
//@RpcService(interfaceType = BlackListService.class)
@DubboService
public class BlackListServiceImpl extends ServiceImpl<BlackListMapper, BlackList> implements BlackListService {
    @Autowired
    BlackListMapper blackListMapper;


    @Override
    public BlackList getBlackListByIp(String ip) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("ip",ip);
        BlackList blackList=blackListMapper.selectOne(queryWrapper);
        return blackList;


    }
}
