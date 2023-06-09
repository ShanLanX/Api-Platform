package com.swx.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.apicommon.model.entity.InterfaceInfo;
import com.swx.springbootinit.service.InterfaceInfoService;
import com.swx.springbootinit.common.ErrorCode;
import com.swx.springbootinit.exception.BusinessException;
import com.swx.springbootinit.exception.ThrowUtils;
import com.swx.springbootinit.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author SunWeixiang
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2023-05-06 21:48:48
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceinfo, boolean add) {
        if (interfaceinfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name=interfaceinfo.getName();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }

    }
}




