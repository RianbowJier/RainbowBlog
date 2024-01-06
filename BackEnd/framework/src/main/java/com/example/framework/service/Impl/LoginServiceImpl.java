package com.example.framework.service.Impl;

import com.example.framework.Utils.JwtUtil;
import com.example.framework.Utils.RedisCache;
import com.example.framework.Utils.SecurityUtils;
import com.example.framework.domain.LoginUser;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.User;
import com.example.framework.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2720:43
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;


    /**
     * 用户登录
     * 1、封装用户信息（账号+密码。。）
     * 2、调用AuthenticationManager的authenticate方法进行验证
     * 3、验证成功后，封装用户信息到LoginUser中
     * 4、生成jwt，返回jwt
     * 5、把用户信息存入redis中
     *
     * @param user
     * @return
     */
    @Override
    public ResponseResult login(User user) {
        // 封装登录的用户名和密码
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());

        //在下一行之前，封装的数据会先走UserDetailsServiceImpl实现类，这个实现类在我们的framework工程的service/impl目录里面

        // 认证用户信息
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 判断用户认证是否通过
        if (authenticate == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 获取用户ID
        String userId = ((LoginUser) authenticate.getPrincipal()).getUser().getId().toString();

        // 生成JWT token
        String jwt = JwtUtil.createJWT(userId);

        // 存储token和用户信息到Redis
        redisCache.setCacheObject("login:" + userId, authenticate.getPrincipal());

        // 返回封装了token的响应结果
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);

        return ResponseResult.okResult(map);
    }

    /**
     * 退出登录
     * 1、获取当前用户的id
     * 2、删除Redis中的用户的token信息
     */
    @Override
    public ResponseResult logout() {
        // 获取用户当前用户id
        Long userId = SecurityUtils.getUserId();

        //删除Redis中对应的值
        redisCache.deleteObject("login:" + userId);

        return ResponseResult.okResult();
    }
}
