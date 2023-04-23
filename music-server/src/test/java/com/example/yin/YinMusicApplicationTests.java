package com.example.yin;

import com.example.yin.model.domain.User;
import com.example.yin.model.reponse.LoginUser;
import com.example.yin.security.service.TokenService;
import com.example.yin.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YinMusicApplicationTests {

    @Resource
    private TokenService tokenService;

    @Test
    public void createToken() {
        LoginUser loginUser = new LoginUser();
        User user = new User();
        user.setId(1);
        user.setUserName("张三");
        user.setPassword("1111");
        user.setSex((byte) 1);
        user.setPhoneNum("111111");
        loginUser.setUser(user);
        String token = tokenService.createToken(loginUser);
        System.out.println(token);
    }
}
