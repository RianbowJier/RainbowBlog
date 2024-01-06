package com.example.framework.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.framework.domain.LoginUser;
import com.example.framework.domain.User;
import com.example.framework.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Description:当BlogLoginServiceImpl类封装好登录的用户名和密码之后，就会传到当前这个实现类
 * @Author:Rainbow
 * @CreateTime:2023/12/1814:35
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 这里之前，我们已经拿到了登录的用户名和密码。
     * UserDetails是SpringSecurity官方提供的接口
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);

        // 如果用户不存在，抛出UsernameNotFoundException
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 返回包装后的用户信息
        return new LoginUser(user);
    }
}