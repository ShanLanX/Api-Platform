package com.swx.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.apiclientsdk.client.ApiClient;

import com.google.gson.Gson;
import com.swx.apicommon.model.entity.InterfaceInfo;
import com.swx.apicommon.model.entity.User;
import com.swx.apicommon.model.entity.UserInterfaceInfo;
import com.swx.springbootinit.annotation.AuthCheck;
import com.swx.springbootinit.annotation.ShanLan;
import com.swx.springbootinit.common.*;
import com.swx.springbootinit.constant.CommonConstant;
import com.swx.springbootinit.constant.UserConstant;
import com.swx.springbootinit.exception.BusinessException;
import com.swx.springbootinit.exception.ThrowUtils;
import com.swx.springbootinit.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.swx.springbootinit.service.InterfaceInfoService;
import com.swx.springbootinit.service.UserInterfaceInfoService;
import com.swx.springbootinit.service.UserService;
import com.swx.springbootinit.common.*;
import com.swx.springbootinit.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.swx.springbootinit.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.swx.springbootinit.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.swx.springbootinit.model.enums.InterfaceInfoStatusEnum;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private ApiClient apiClient;

    @Resource
    private RedisTemplate redisTemplate;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceinfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceinfoAddRequest, HttpServletRequest request) {
        if (interfaceinfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceinfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceinfoAddRequest, interfaceinfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceinfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceinfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceinfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceInfoId = interfaceinfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfaceinfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceinfoUpdateRequest) {
        if (interfaceinfoUpdateRequest == null || interfaceinfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceinfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceinfoUpdateRequest, interfaceinfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceinfo, false);
        long id = interfaceinfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfaceInfoService.updateById(interfaceinfo);

        return ResultUtils.success(result);
    }
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfoQuery.getDescription();
        // description 需支持模糊搜索
        interfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }
    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        return ResultUtils.success(interfaceInfo);
    }

    /**
     *发布一个接口
     * @param
     * @return
     */

    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest , HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id=idRequest.getId();
        // 判断接口是否存在
        InterfaceInfo oldInterfaceInfo=interfaceInfoService.getById(id);
        if(oldInterfaceInfo==null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User userLogin=userService.getLoginUser(request);
        com.example.apiclientsdk.model.User user=new com.example.apiclientsdk.model.User(userLogin.getUserName(),userLogin.getUserPassword());
        String username=apiClient.getUserNameByPost(user);
        if(StringUtils.isBlank(username)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口验证失败");

        }
        InterfaceInfo interfaceInfo=new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }
    /**
     *下线一个接口
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest ,HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id=idRequest.getId();
        // 判断接口是否存在
        InterfaceInfo oldInterfaceInfo=interfaceInfoService.getById(id);
        if(oldInterfaceInfo==null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        InterfaceInfo interfaceInfo=new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest invokeRequest , HttpServletRequest request) {
        if (invokeRequest == null || invokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id=invokeRequest.getId();
        // 判断接口是否存在
        InterfaceInfo oldInterfaceInfo=interfaceInfoService.getById(id);
        if(oldInterfaceInfo==null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 判断接口状态
        if(oldInterfaceInfo.getStatus()==InterfaceInfoStatusEnum.OFFLINE.getValue()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口已关闭！");

        }
        String userRequestParams=invokeRequest.getUserRequestParams();
        User loginUser=userService.getLoginUser(request);
        QueryWrapper<UserInterfaceInfo> userInterfaceInfoQueryWrapper=new QueryWrapper<>();
        userInterfaceInfoQueryWrapper.eq("userId",loginUser.getId());
        userInterfaceInfoQueryWrapper.eq("interfaceInfoId",id);
        UserInterfaceInfo userInterfaceInfo=userInterfaceInfoService.getOne(userInterfaceInfoQueryWrapper);
        if(userInterfaceInfo==null||userInterfaceInfo.getLeftNum()<=0){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"调用次数不足！");

        }
        String accessKey=loginUser.getAccessKey();
        String secretKey=loginUser.getSecretKey();
        Object res=invokeInterfaceInfo(ApiClient.class,oldInterfaceInfo.getMethodName(),userRequestParams,accessKey,secretKey);
        if(res==null){
            throw  new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if(res.toString().contains("Error request"))
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"调用错误，请检查调用次数");
        }

//        Gson gson=new Gson();
//        com.example.apiclientsdk.model.User user=gson.fromJson(userRequestParams, com.example.apiclientsdk.model.User.class);
//        String userNameByPost=tempClient.getUserNameByPost(user);
        return ResultUtils.success(res);
    }
    private Object invokeInterfaceInfo(Class<?> clazz,String methodName,String userRequestParams,String accessKey,String secretKey){
        try{
            Constructor<?> clientConstructor =clazz.getConstructor(String.class,String.class);
            ApiClient apiClient= (ApiClient) clientConstructor.newInstance(accessKey,secretKey);
            Method[] methods=clazz.getMethods();
            for(Method m:methods){
                if(m.getName().equals(methodName)){
                    Class<?>[] paramTypes=m.getParameterTypes();
                    if(paramTypes.length==0){
                        // 无参方法直接调用
                        return m.invoke(apiClient);
                    }
                    Gson gson=new Gson();
                    Object parameter=gson.fromJson(userRequestParams,paramTypes[0]);
                    return m.invoke(apiClient,parameter);

                }
            }



        }
        catch (Exception e){
            e.printStackTrace();
            throw  new BusinessException(ErrorCode.SYSTEM_ERROR,"调用方法出错");

        }
        return null;
    }

}
