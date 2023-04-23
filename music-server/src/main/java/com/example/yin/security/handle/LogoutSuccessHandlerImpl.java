package com.example.yin.security.handle;

import com.example.yin.model.reponse.LoginUser;
import com.example.yin.security.service.TokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author whf
 * @date 2023/4/20
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Resource
    private TokenService tokenService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //获取用户信息
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (Objects.nonNull(loginUser)) {
            // 删除用户缓存
            tokenService.delLoginUser(loginUser);
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print("退出成功");
        }
    }
}
