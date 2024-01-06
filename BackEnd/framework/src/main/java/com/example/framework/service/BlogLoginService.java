package com.example.framework.service;

import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.User;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/1814:33
 */
public interface BlogLoginService {
    ResponseResult login(User user);


    ResponseResult logout();
}