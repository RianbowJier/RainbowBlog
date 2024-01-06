package com.example.framework.service.Impl;

import com.example.framework.Utils.BeanCopyUtils;
import com.example.framework.Utils.JwtUtil;
import com.example.framework.Utils.RedisCache;
import com.example.framework.domain.LoginUser;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.User;
import com.example.framework.domain.vo.BlogUserLoginVo;
import com.example.framework.domain.vo.UserInfoVo;
import com.example.framework.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Description:认证，判断用户登录是否成功
 * @Author:Rainbow
 * @CreateTime:2023/12/1814:34
 */
@Service
public class BlogLoginServiceImpl implements BlogLoginService {
    @Autowired
    //AuthenticationManager是security官方提供的接口
    private AuthenticationManager authenticationManager;

    @Autowired
    //RedisCache是config目录写的类
    private RedisCache redisCache;

    /**
     * 用户信息校验并生成token
     *
     * @param user
     */
    @Override
    public ResponseResult login(User user) {
        // 封装登录的用户名和密码
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());

        //封装的数据会先走UserDetailsServiceImpl实现类

        // 进行用户认证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 如果认证失败，抛出异常
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 获取登录用户信息及其ID
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();

        // 生成JWT令牌
        String jwt = JwtUtil.createJWT(userId);

        // 存储用户信息到Redis
        //下面那行的第一个参数: 把上面那行的jwt，也就是token值保存到Redis。存到时候是键值对的形式，值就是jwt，key要加上 "bloglogin:" 前缀
        //下面那行的第二个参数: 要把哪个对象存入Redis。我们写的是loginUser，里面有权限信息，后面会用到
        redisCache.setCacheObject("bloglogin:" + userId, loginUser);

        // 准备响应数据：用户信息和JWT令牌
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo vo = new BlogUserLoginVo(jwt, userInfoVo);

        // 封装响应返回
        return ResponseResult.okResult(vo);
    }


    /**
     * 退出登录接口
     *
     * @return
     */
    @Override
    public ResponseResult logout() {
        // 获取当前用户对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        // 获取userid
        Long userid = loginUser.getUser().getId();

        // 在Redis根据key来删除用户的value值，注意之前我们在存key的时候，key是加了'bloglogin:'前缀
        redisCache.deleteObject("bloglogin:" + userid);
        // 封装响应返回
        return ResponseResult.okResult();
    }
}