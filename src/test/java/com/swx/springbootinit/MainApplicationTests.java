package com.swx.springbootinit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.swx.apicommon.model.entity.User;

import javax.annotation.Resource;

import com.swx.springbootinit.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class MainApplicationTests {


    @Resource
    UserMapper userMapper;

    @Test
    void contextLoads() {
        QueryWrapper<User> q=new QueryWrapper<>();
        q.eq("accessKey","swx");
        User user=userMapper.selectOne(q);
        System.out.println(user);
    }

}
