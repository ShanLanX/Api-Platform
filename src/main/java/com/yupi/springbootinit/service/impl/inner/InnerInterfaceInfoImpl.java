package com.yupi.springbootinit.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.swx.apicommon.model.entity.InterfaceInfo;
import com.swx.apicommon.service.InnerInterfaceInfoService;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.mapper.InterfaceInfoMapper;
import com.yupi.springbootinit.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;


@DubboService
public class InnerInterfaceInfoImpl implements InnerInterfaceInfoService {
    @Resource
    InterfaceInfoMapper interfaceInfoMapper;



    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", method);
        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}
