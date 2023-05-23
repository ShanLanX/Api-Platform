package com.yupi.springbootinit.service.impl.inner;

import com.swx.apicommon.service.InnerInterfaceInfoService;
import com.swx.apicommon.service.InnerUserInterfaceInfoService;
import com.yupi.springbootinit.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
@DubboService
public class InnerUserInterfaceInfoImpl implements InnerUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }
}
