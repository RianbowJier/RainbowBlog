package com.example.blog.controller;

import com.example.framework.Utils.BeanCopyUtils;
import com.example.framework.annotation.Systemlog;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.User;
import com.example.framework.domain.dto.UserDto;
import com.example.framework.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/1922:06
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户信息的相关接口文档")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 获取用户信息
     */
    @GetMapping("/userInfo")
    @Systemlog(businessName = "获取用户信息接口")
    @ApiOperation(value = "获取用户信息接口")
    public ResponseResult userInfo() {
        return userService.userInfo();
    }


    /**
     * 更新用户信息
     *
     * @param userDto
     * @return
     */
    @PutMapping("userInfo")
    @ApiOperation(value = "更新用户信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userDto", value = "用户实体类"),
    })
    public ResponseResult updateUserInfo(@RequestBody UserDto userDto) {
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        //更新个人信息
        return userService.updateUserInfo(user);
    }

    /**
     * 注册新用户
     *
     * @param userDto
     * @return
     */
    @PostMapping("/register")
    @Systemlog(businessName = "注册新用户接口")
    @ApiOperation(value = "注册新用户接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userDto", value = "用户实体类"),
    })
    public ResponseResult register(@RequestBody UserDto userDto) {
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        //注册功能
        return userService.register(user);
    }
}
