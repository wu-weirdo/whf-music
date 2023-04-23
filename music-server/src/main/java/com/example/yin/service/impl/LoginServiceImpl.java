package com.example.yin.service.impl;

import com.example.yin.constant.ResultEnum;
import com.example.yin.excepetion.ServiceException;
import com.example.yin.model.domain.User;
import com.example.yin.model.reponse.LoginUser;
import com.example.yin.model.request.LoginRequest;
import com.example.yin.model.request.RegisterRequest;
import com.example.yin.security.service.TokenService;
import com.example.yin.security.utils.SecurityUtils;
import com.example.yin.service.LoginService;
import com.example.yin.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author whf
 * @date 2023/4/21
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private TokenService tokenService;

    @Resource
    private UserService userService;

    /**
     * 登录
     *
     * @param request 请求
     * @return {@code String}
     */
    @Override
    public LoginUser login(LoginRequest request) {
        // 用户验证
        Authentication authenticate = null;

        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword());
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            throw new ServiceException("用户名或密码错误");
        }
        // 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String token = tokenService.createToken(loginUser);
        loginUser.setToken(token);
        return loginUser;
    }

    /**
     * 注册
     *
     * @param request 请求
     * @return {@code Object}
     */
    @Override
    public Boolean register(RegisterRequest request) {
        User user = new User();
        BeanUtils.copyProperties(request, user);
        if (userService.existUser(request.getUserName())) {
            throw new ServiceException(ResultEnum.PARAMETER_ERROR.getCode(), "用户名已注册");
        }
        String password = SecurityUtils.encryptPassword(request.getPassword());
        user.setPassword(password);
        user.setAvator("/resource/img/avatorImages/user.jpg");
        user.setCreateTime(new Date());
        return userService.save(user);
    }
}
