package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.apicommon.model.entity.UserInterfaceInfo;

import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;

import com.yupi.springbootinit.mapper.UserInterfaceInfoMapper;
import com.yupi.springbootinit.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

/**
* @author SunWeixiang
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2023-05-14 14:55:11
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {


    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 创建时，校验用户和接口
        if (add) {
            if(userInterfaceInfo.getUserId()<=0||userInterfaceInfo.getInterfaceInfoId()<=0)
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户或接口不存在");


        }
        if (userInterfaceInfo.getLeftNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能小于 0");
        }
    }

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if(interfaceInfoId<=0||userId<=0){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId",interfaceInfoId);
        updateWrapper.eq("userId",userId);
        updateWrapper.gt("leftNum",0);
        updateWrapper.setSql("leftNum=leftNum-1,totalNum=totalNum-1");
        return this.update(updateWrapper);
    }
}




