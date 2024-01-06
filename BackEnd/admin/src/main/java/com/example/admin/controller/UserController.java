package com.example.admin.controller;

import com.example.framework.Utils.SecurityUtils;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.Role;
import com.example.framework.domain.User;
import com.example.framework.domain.enums.AppHttpCodeEnum;
import com.example.framework.domain.exception.SystemException;
import com.example.framework.domain.vo.UserInfoAndRoleIdsVo;
import com.example.framework.service.RoleService;
import com.example.framework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2921:39
 */
@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * 查询用户列表
     *
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(User user, Integer pageNum, Integer pageSize) {
        return userService.selectUserPage(user, pageNum, pageSize);
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody User user) {
        //校验用户名唯一性
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!userService.checkUserNameUnique(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        //校验手机号唯一性
        if (!userService.checkPhoneUnique(user)) {
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        //校验邮箱唯一性
        if (!userService.checkEmailUnique(user)) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        return userService.addUser(user);
    }

    //-----------------------修改用户-①根据id查询用户信息-----------------------------

    /**
     * 删除用户
     *
     * @param userIds
     * @return
     */
    @DeleteMapping("/{userIds}")
    public ResponseResult remove(@PathVariable List<Long> userIds) {
        if (userIds.contains(SecurityUtils.getUserId())) {
            return ResponseResult.errorResult(500, "不能删除当前你正在使用的用户");
        }
        //根据用户id删除sys_user表中的数据
        userService.removeByIds(userIds);

        return ResponseResult.okResult();
    }

    /**
     * 修改用户-①根据id查询用户信息
     *
     * @param userId
     * @return
     */
    @GetMapping(value = {"/{userId}"})
    public ResponseResult getUserInfoAndRoleIds(@PathVariable(value = "userId") Long userId) {
        List<Role> roles = roleService.selectRoleAll();
        User user = userService.getById(userId);
        //当前用户所具有的角色id列表
        List<Long> roleIds = roleService.selectRoleIdByUserId(userId);

        UserInfoAndRoleIdsVo vo = new UserInfoAndRoleIdsVo(user, roles, roleIds);
        return ResponseResult.okResult(vo);
    }

    /**
     * 修改用户-②更新用户信息
     *
     * @param user
     * @return
     */
    @PutMapping
    public ResponseResult edit(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseResult.okResult();
    }

}
