package com.example.yin.security.service;

import com.example.yin.constant.Constants;
import com.example.yin.model.reponse.LoginUser;
import com.example.yin.utils.IdUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author whf
 * @date 2023/4/20
 */
@Component
public class TokenService {

    /**
     * 令牌自定义标识
     */
    @Value("${token.header}")
    private String header;

    /**
     * 令牌秘钥
     */
    @Value("${token.secret}")
    private String secret;

    /**
     * 令牌有效期（默认30分钟）
     */
    @Value("${token.expireTime}")
    private int expireTime;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Resource
    private RedisTemplate<String, LoginUser> redisTemplate;

    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = getToken(request);
        if (StringUtils.hasText(token)) {
            Claims claims = parseToken(token);
            // 解析对应的权限以及用户信息
            String tokenValue = (String) claims.get(Constants.LOGIN_USER_KEY);
            return redisTemplate.opsForValue().get(Constants.LOGIN_TOKEN_KEY + tokenValue);
        }
        return null;
    }

    /**
     * 删除用户身份信息
     *
     * @param loginUser 登录用户
     */
    public void delLoginUser(LoginUser loginUser) {
        redisTemplate.delete(Constants.LOGIN_TOKEN_KEY + loginUser.getToken());
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        refreshToken(loginUser);
        Map<String, Object> claims = Collections.singletonMap(Constants.LOGIN_USER_KEY, token);
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token)
    {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser 登录用户
     */
    public void validateToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN)
        {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录用户
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setExpireTime(System.currentTimeMillis() + (long) expireTime * 60 * 1000);
        // 根据用户token 重新设置缓存
        redisTemplate.opsForValue().set(Constants.LOGIN_TOKEN_KEY + loginUser.getToken(), loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 获取请求token
     *
     * @param request 请求
     * @return {@code String}
     */
    private String getToken(HttpServletRequest request) {
       return request.getHeader(header);
    }
}
