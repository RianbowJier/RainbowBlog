package com.example.framework.service;

import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.User;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2720:42
 */
public interface LoginService {
    ResponseResult logout();

    ResponseResult login(User user);
}
