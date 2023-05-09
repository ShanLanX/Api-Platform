package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.springbootinit.model.entity.InterfaceInfo;

/**
* @author SunWeixiang
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-05-06 21:48:48
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceinfo, boolean b);
}
