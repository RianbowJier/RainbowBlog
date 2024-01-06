package com.example.framework.handler.security;

import com.alibaba.fastjson.JSON;
import com.example.framework.Utils.WebUtils;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.enums.AppHttpCodeEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description:自定义官方提供的授权失败处理器
 * @Author:Rainbow
 * @CreateTime:2023/12/1916:19
 */
@Component
// 处理访问被拒绝的处理器。ResponseResult、AppHttpCodeEnum是我们在frame-work工程写的类
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    // 处理访问被拒绝的方法
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 打印访问被拒绝的异常信息
        accessDeniedException.printStackTrace();

        // 返回无操作权限的错误信息
        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);

        // 将响应结果转换成JSON格式，然后响应给前端
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}