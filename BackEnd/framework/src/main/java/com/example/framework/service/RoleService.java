package com.example.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.Role;

import java.util.List;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2721:23
 */
public interface RoleService extends IService<Role> {
    /**
     * 查询用户的角色信息
     *
     * @param id
     * @return
     */
    List<String> selectRoleKeyByUserId(Long id);

    /**
     * 查询角色列表
     *
     * @param role
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize);


    /**
     * 新增用户-①查询角色列表接口
     *
     * @return
     */
    List<Role> selectRoleAll();
    

    /**
     * 修改用户-①根据id查询用户信息
     *
     * @param userId
     * @return
     */
    List<Long> selectRoleIdByUserId(Long userId);

}