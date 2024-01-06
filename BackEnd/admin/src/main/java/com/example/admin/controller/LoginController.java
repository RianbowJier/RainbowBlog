package com.example.admin.controller;

import com.example.framework.Utils.BeanCopyUtils;
import com.example.framework.Utils.RedisCache;
import com.example.framework.Utils.SecurityUtils;
import com.example.framework.domain.LoginUser;
import com.example.framework.domain.Menu;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.User;
import com.example.framework.domain.dto.UserDto;
import com.example.framework.domain.enums.AppHttpCodeEnum;
import com.example.framework.domain.exception.SystemException;
import com.example.framework.domain.vo.AdminUserInfoVo;
import com.example.framework.domain.vo.RoutersVo;
import com.example.framework.domain.vo.UserInfoVo;
import com.example.framework.service.LoginService;
import com.example.framework.service.MenuService;
import com.example.framework.service.RoleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:登录
 * @Author:Rainbow
 * @CreateTime:2023/12/1814:38
 */
@RestController
@Api(tags = "登录的相关接口文档")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisCache redisCache;    //Redis缓存

    /**
     * 管理员登录
     *
     * @param userDto
     * @return
     */
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody UserDto userDto) {
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        if (!StringUtils.hasText(user.getUserName())) {
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    /**
     * 查询(超级管理员|非超级管理员)的权息限和角色信
     *
     * @return
     */
    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());

        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        //获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        //封装响应返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    /**
     * 查询路由信息(权限菜单)
     *
     * @return
     */
    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters() {
        //获取用户id
        Long userId = SecurityUtils.getUserId();

        //根据用户id来查询menu(权限菜单)。要求查询结果是tree的形式，也就是子父菜单树
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装响应返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    /**
     * 退出登录
     *
     * @return
     */
    @PostMapping("/user/logout")
    public ResponseResult logout() {
        //调用业务层退出登录
        return loginService.logout();
    }


}