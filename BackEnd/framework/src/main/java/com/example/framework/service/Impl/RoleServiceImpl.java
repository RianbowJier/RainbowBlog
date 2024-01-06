package com.example.framework.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.framework.constants.SystemConstants;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.Role;
import com.example.framework.domain.vo.PageVo;
import com.example.framework.mapper.RoleMapper;
import com.example.framework.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2721:23
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 查询用户的角色信息
     *
     * @param id
     * @return
     */
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员，如果是，就返回集合中只需要有admin
        if (id == 1L) {
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }

        //否则查询对应普通用户所具有的的角色信息
        List<String> otherRole = roleMapper.selectRoleKeyByOtherUserId(id);

        return otherRole;
    }


    /**
     * 查询角色列表
     *
     * @param role
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.like(StringUtils.hasText(role.getRoleName()), Role::getRoleName, role.getRoleName())
                .eq(StringUtils.hasText(role.getStatus()), Role::getStatus, role.getStatus())
                .orderByAsc(Role::getRoleSort);


        Page<Role> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, lambdaQueryWrapper);

        //转换成VO
        List<Role> roles = page.getRecords();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(roles);
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 新增用户-①查询角色列表接口
     *
     * @return
     */
    @Override
    public List<Role> selectRoleAll() {
        return list(Wrappers.<Role>lambdaQuery().eq(Role::getStatus, SystemConstants.NORMAL));
    }

    /**
     * 修改用户-①根据id查询用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public List<Long> selectRoleIdByUserId(Long userId) {
        return getBaseMapper().selectRoleIdByUserId(userId);
    }
}