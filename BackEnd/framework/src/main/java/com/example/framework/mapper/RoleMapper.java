package com.example.framework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.framework.domain.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2721:24
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 查询普通用户的角色权限
     *
     * @param userId
     * @return
     */
    @Select("SELECT r.role_key " +
            "FROM sys_user_role ur " +
            "LEFT JOIN sys_role r ON ur.role_id = r.id " +
            "WHERE ur.user_id = #{userId} AND r.status = 0 AND r.del_flag = 0")
    List<String> selectRoleKeyByOtherUserId(@Param("userId") Long userId);


    /**
     * 修改用户-①根据id查询用户角色信息
     *
     * @param userId
     * @return
     */
    @Select("select r.id from sys_role r " +
            "left join sys_user_role ur on ur.role_id = r.id " +
            "where ur.user_id = #{userId}")
    List<Long> selectRoleIdByUserId(@Param("userId") Long userId);
}