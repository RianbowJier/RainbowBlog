package com.example.blog.filter;


import com.alibaba.fastjson.JSON;
import com.example.framework.Utils.JwtUtil;
import com.example.framework.Utils.RedisCache;
import com.example.framework.Utils.WebUtils;
import com.example.framework.domain.LoginUser;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.enums.AppHttpCodeEnum;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/1815:42
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
// 注入RedisCache，用于操作Redis
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取请求头中的token值
        String token = request.getHeader("token");

        // 如果token为空或为空字符串，说明该接直口不需要登录，接放行，不拦截
        if (StringUtils.hasText(token)) {
            try {
                // 使用JwtUtil解析token，获取其中的Claims（声明）
                Claims claims = JwtUtil.parseJWT(token);

                // 获取token中的用户ID
                String userId = claims.getSubject();

                // 在Redis中通过用户ID获取对应的登录用户
                LoginUser loginUser = redisCache.getCacheObject("bloglogin:" + userId);

                // 如果Redis中不存在该用户，说明登录已过期，需要重新登录
                if (Objects.isNull(loginUser)) {
                    ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
                    WebUtils.renderString(response, JSON.toJSONString(result));
                    return;
                }

                // 将用户对象存储到SecurityContextHolder中
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } catch (Exception e) {
                // 如果解析token时发生异常，说明token已被篡改或已过期，需要重新登录
                e.printStackTrace();
                ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
                WebUtils.renderString(response, JSON.toJSONString(result));
                return;
            }
        }

        // 处理请求
        filterChain.doFilter(request, response);
    }
}