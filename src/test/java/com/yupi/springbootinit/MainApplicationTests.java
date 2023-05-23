package com.yupi.springbootinit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.swx.apicommon.model.entity.User;
import com.swx.apicommon.service.InnerUserService;
import com.yupi.springbootinit.config.WxOpenConfig;
import javax.annotation.Resource;

import com.yupi.springbootinit.mapper.UserMapper;
import com.yupi.springbootinit.service.UserService;
import com.yupi.springbootinit.service.impl.inner.InnerUserServiceImpl;
import org.checkerframework.checker.signature.qual.PolySignature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

/**
 * 主类测试
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
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
