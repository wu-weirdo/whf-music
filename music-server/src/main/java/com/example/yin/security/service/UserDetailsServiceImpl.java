package com.example.yin.security.service;

import com.example.yin.model.domain.User;
import com.example.yin.model.reponse.LoginUser;
import com.example.yin.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author whf
 * @date 2023/4/20
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.selectUserByUserName(username);
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }
        return new LoginUser(user);
    }
}
