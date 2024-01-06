package com.example.framework.handler.security;

import com.alibaba.fastjson.JSON;
import com.example.framework.Utils.WebUtils;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.enums.AppHttpCodeEnum;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description:自定义官方提供的认证失败处理器，没有token就访问其他页面
 * @Author:Rainbow
 * @CreateTime:2023/12/1916:18
 */

@Component
// 自定义认证失败的处理器。ResponseResult、AppHttpCodeEnum是我们在frame-work工程写的类
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    // 处理认证异常的入口方法
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 打印异常信息
        authException.printStackTrace();

        // 初始化响应结果
        ResponseResult result;

        // 根据不同的认证异常类型，返回相应的错误信息
        if (authException instanceof BadCredentialsException) {
            // 用户名或密码错误的错误信息
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(), authException.getMessage());
        } else if (authException instanceof InsufficientAuthenticationException) {
            // 需要登录后访问的错误信息，没有token
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        } else {
            // 默认的认证或授权失败的错误信息
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "认证或授权失败");
        }

        // 将响应结果转换成JSON格式，然后响应给前端
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}
