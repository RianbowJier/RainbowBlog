package com.example.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.User;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/1917:52
 */
public interface UserService extends IService<User> {
    //个人信息查询
    ResponseResult userInfo();

    //更新个人信息
    ResponseResult updateUserInfo(User user);

    //用户注册功能
    ResponseResult register(User user);


    /**
     * 查询用户列表
     *
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize);

    /**
     * 校验用户名是否唯一
     *
     * @param userName
     * @return
     */
    boolean checkUserNameUnique(String userName);


    /**
     * 校验手机号是否唯一
     *
     * @param user
     * @return
     */
    boolean checkPhoneUnique(User user);

    /**
     * 校验邮箱是否唯一
     *
     * @param user
     * @return
     */
    boolean checkEmailUnique(User user);

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    ResponseResult addUser(User user);


    /**
     * 修改用户-②更新用户信息
     *
     * @param user
     */
    void updateUser(User user);
}