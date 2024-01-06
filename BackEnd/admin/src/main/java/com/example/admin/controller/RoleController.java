package com.example.admin.controller;

import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.Role;
import com.example.framework.domain.dto.ChangeRoleStatusDto;
import com.example.framework.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2921:32
 */
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;


    /**
     * 查询角色列表
     *
     * @param role
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Role role, Integer pageNum, Integer pageSize) {
        return roleService.selectRolePage(role, pageNum, pageSize);
    }

    /**
     * 修改角色状态
     *
     * @param roleStatusDto
     * @return
     */
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeRoleStatusDto roleStatusDto) {
        Role role = new Role();
        role.setId(roleStatusDto.getRoleId());
        role.setStatus(roleStatusDto.getStatus());

        return ResponseResult.okResult(roleService.updateById(role));
    }


    /**
     * 查询角色列表接口
     *
     * @return
     */

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole() {
        List<Role> roles = roleService.selectRoleAll();
        return ResponseResult.okResult(roles);
    }


}
