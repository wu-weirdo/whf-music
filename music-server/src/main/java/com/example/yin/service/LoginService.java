package com.example.yin.service;

import com.example.yin.model.reponse.LoginUser;
import com.example.yin.model.request.LoginRequest;
import com.example.yin.model.request.RegisterRequest;

/**
 * @author whf
 * @date 2023/4/21
 */
public interface LoginService {

    /**
     * 登录
     *
     * @param request 请求
     * @return {@code LoginUser}
     */
    LoginUser login(LoginRequest request);

    /**
     * 注册
     *
     * @param request 请求
     * @return {@code Object}
     */
    Boolean register(RegisterRequest request);
}
