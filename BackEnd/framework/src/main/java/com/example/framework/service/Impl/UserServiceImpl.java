package com.example.framework.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.framework.Utils.BeanCopyUtils;
import com.example.framework.Utils.SecurityUtils;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.User;
import com.example.framework.domain.UserRole;
import com.example.framework.domain.enums.AppHttpCodeEnum;
import com.example.framework.domain.exception.SystemException;
import com.example.framework.domain.vo.PageVo;
import com.example.framework.domain.vo.UserInfoVo;
import com.example.framework.domain.vo.UserVo;
import com.example.framework.mapper.UserMapper;
import com.example.framework.service.UserRoleService;
import com.example.framework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/1917:52
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 查询个人信息
     */
    @Override
    public ResponseResult userInfo() {
        //获取当前用户的用户id。
        Long userId = SecurityUtils.getUserId();

        //根据用户id查询用户信息
        User user = getById(userId);

        //封装成UserInfoVo(我们在framework工程写的类)，然后返回
        UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    /**
     * 更细用户信息
     *
     * @param user
     * @return
     */
    @Override
    @Transactional
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    /**
     * 用户注册功能的具体代码
     *
     * @param user
     * @return
     */
    @Override
    public ResponseResult register(User user) {

        //对前端传过来的用户名进行非空判断，例如null、""，就抛出异常
        if (!StringUtils.hasText(user.getUserName())) {
            //SystemException是我们写的异常类、AppHttpCodeEnum是我们写的枚举类
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        //密码
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        //邮箱
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        //昵称
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //判断用户传给我们的用户名是否在数据库已经存在。userNameExist方法是下面定义的
        if (userNameExist(user.getUserName())) {
            //SystemException是我们写的异常类、AppHttpCodeEnum是我们写的枚举类
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        //判断用户传给我们的昵称是否在数据库已经存在。NickNameExist方法是下面定义的
        if (NickNameExist(user.getNickName())) {
            //SystemException是我们写的异常类、AppHttpCodeEnum是我们写的枚举类
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //判断用户传给我们的邮箱是否在数据库已经存在。NickNameExist方法是下面定义的
        if (EmailExist(user.getEmail())) {
            //SystemException是我们写的异常类、AppHttpCodeEnum是我们写的枚举类
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        //经过上面的判断，可以确保用户传给我们的用户名和昵称是数据库不存在的，且相关字段都不为空。就可以存入数据库
        //注意用户传给我们的密码是明文，对于密码我们要转成密文之后再存入数据库。注意加密要和解密用同一套算法
        //在huanf-blog工程的securityConfig类里面有解密算法，当时我们写了一个passwordEncoder方法，并且注入到了spring容器
        String encodePassword = passwordEncoder.encode(user.getPassword());//加密
        user.setPassword(encodePassword);
        //存入数据库。save方法是mybatisplus提供的方法
        save(user);

        user.setRoleIds(new Long[2]);
        //默认注册用户角色为普通用户
        addUser(user);


        //封装响应返回
        return ResponseResult.okResult();
    }

    //'判断用户传给我们的用户名是否在数据库已经存在' 的方法
    private boolean userNameExist(String userName) {
        //要知道是否存在，首先就是根据条件去数据库查
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //拿用户写的用户名跟数据库里面的用户名进行比较
        queryWrapper.eq(User::getUserName, userName);
        //如果大于0就说明从数据库查出来了，也就说明是已经存在数据库的
        boolean result = count(queryWrapper) > 0;
        //为true就说明已存在
        return result;
    }

    //'判断用户传给我们的昵称是否在数据库已经存在' 的方法
    private boolean NickNameExist(String nickName) {
        //要知道是否存在，首先就是根据条件去数据库查
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //拿用户写的昵称跟数据库里面的昵称进行比较
        queryWrapper.eq(User::getNickName, nickName);
        //如果大于0就说明从数据库查出来了，也就说明是已经存在数据库的
        boolean result = count(queryWrapper) > 0;
        //为true就说明已存在
        return result;
    }

    //'判断用户传给我们的邮箱是否在数据库已经存在' 的方法
    private boolean EmailExist(String email) {
        //要知道是否存在，首先就是根据条件去数据库查
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //拿用户写的昵称跟数据库里面的昵称进行比较
        queryWrapper.eq(User::getEmail, email);
        //如果大于0就说明从数据库查出来了，也就说明是已经存在数据库的
        boolean result = count(queryWrapper) > 0;
        //为true就说明已存在
        return result;
    }

    //'判断用户传给我们的手机号码是否在数据库已经存在' 的方法
    private boolean PhonenumberExist(String Phonenumber) {
        //要知道是否存在，首先就是根据条件去数据库查
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //拿用户写的手机号码跟数据库里面的手机号码进行比较
        queryWrapper.eq(User::getPhonenumber, Phonenumber);
        //如果大于0就说明从数据库查出来了，也就说明是已经存在数据库的
        boolean result = count(queryWrapper) > 0;
        //为true就说明已存在
        return result;
    }

    /**
     * 用户列表
     *
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(user.getUserName()), User::getUserName, user.getUserName())
                .eq(StringUtils.hasText(user.getStatus()), User::getStatus, user.getStatus())
                .eq(StringUtils.hasText(user.getPhonenumber()), User::getPhonenumber, user.getPhonenumber());

        Page<User> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);

        //转换成VO
        List<User> users = page.getRecords();
        List<UserVo> userVoList = users.stream()
                .map(u -> BeanCopyUtils.copyBean(u, UserVo.class))
                .collect(Collectors.toList());
        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(userVoList);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getUserName, userName)) == 0;
    }

    @Override
    public boolean checkPhoneUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getPhonenumber, user.getPhonenumber())) == 0;
    }

    @Override
    public boolean checkEmailUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getEmail, user.getEmail())) == 0;
    }

    /**
     * 新增用户信息
     *
     * @param user
     * @return
     */
    @Override
    @Transactional
    public ResponseResult addUser(User user) {
        //密码加密处理
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);

        if (user.getRoleIds() != null && user.getRoleIds().length > 0) {
            //新增用户对应的角色信息
            insertUserRole(user);
        }
        return ResponseResult.okResult();
    }


    /**
     * 新增用户对应的角色信息
     *
     * @param user
     */
    private void insertUserRole(User user) {
        List<UserRole> sysUserRoles = Arrays.stream(user.getRoleIds())
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        System.out.println(user);
        userRoleService.saveBatch(sysUserRoles);
    }


    /**
     * 修改用户-②更新用户信息
     *
     * @param user
     */
    @Override
    @Transactional
    public void updateUser(User user) {
        // 删除用户与角色关联
        LambdaQueryWrapper<UserRole> userRoleUpdateWrapper = new LambdaQueryWrapper<>();
        userRoleUpdateWrapper.eq(UserRole::getUserId, user.getId());
        userRoleService.remove(userRoleUpdateWrapper);

        // 新增用户与角色管理
        insertUserRole(user);
        // 更新用户信息
        updateById(user);
    }
}