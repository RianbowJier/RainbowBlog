package com.example.blog.controller;

import com.example.framework.Utils.BeanCopyUtils;
import com.example.framework.annotation.Systemlog;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.User;
import com.example.framework.domain.dto.UserDto;
import com.example.framework.domain.enums.AppHttpCodeEnum;
import com.example.framework.domain.exception.SystemException;
import com.example.framework.service.BlogLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:登录
 * @Author:Rainbow
 * @CreateTime:2023/12/1814:38
 */
@RestController
@Api(tags = "登录的相关接口文档")
public class BlogLoginController {

    @Autowired
    //BlogLoginService是我们在service目录写的接口
    private BlogLoginService blogLoginService;


    /**
     * 登录接口
     *
     * @param userDto
     * @return
     */
    @PostMapping("/login")
    @Systemlog(businessName = "登录接口")
    @ApiOperation(value = "登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userDto", value = "用户实体类"),
    })
    public ResponseResult login(@RequestBody UserDto userDto) {
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        //如果用户在进行登录时，没有传入'用户名'
        if (!StringUtils.hasText(user.getUserName())) {
            // 提示'必须要传用户名'。AppHttpCodeEnum是我们写的枚举类。SystemException是我们写的统一异常处理的类
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    /**
     * 退出登录接口
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "退出接口")
    public ResponseResult logout() {
        return blogLoginService.logout();
    }
}