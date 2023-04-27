package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.model.reponse.LoginUser;
import com.example.yin.model.request.LoginRequest;
import com.example.yin.model.request.RegisterRequest;
import com.example.yin.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

import javax.annotation.Resource;

/**
 * @author whf
 * @date 2023/4/21
 */
@RestController
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("/login")
    public R<LoginUser> login(@Valid @RequestBody LoginRequest request) {
        return R.success(loginService.login(request), "登录成功");
    }

    @PostMapping("/register")
    public R<Boolean> register(@Valid @RequestBody RegisterRequest request) {
        Boolean result = loginService.register(request);
        if (result) {
            return R.success(true, "注册成功");
        } else {
            return R.error("注册失败");
        }
    }
}
