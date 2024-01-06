package com.example.framework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.framework.domain.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * &#064;Description:
 *
 * @Author:Rainbow
 * @CreateTime:2023/12/2721:28
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    //

    /**
     * 查询普通用户的权限信息
     *
     * @param userId
     * @return
     */
    @Select("SELECT DISTINCT m.perms " +
            "FROM sys_user_role ur " +
            "LEFT JOIN sys_role_menu rm ON ur.role_id = rm.role_id " +
            "LEFT JOIN sys_menu m ON m.id = rm.menu_id " +
            "WHERE ur.user_id = #{userId} AND m.menu_type IN ('C','F') AND m.status = 0 AND m.del_flag = 0")
    List<String> selectPermsByOtherUserId(Long userId);


    /**
     * 查询超级管理员的路由信息(权限菜单)
     *
     * @return
     */
    @Select("SELECT DISTINCT m.id, m.parent_id, m.menu_name, m.path, m.component, m.visible, m.status, " +
            "IFNULL(m.perms,'') AS perms, m.is_frame,  m.menu_type, m.icon, m.order_num, m.create_time " +
            "FROM sys_menu m " +
            "WHERE m.menu_type IN ('C','M') AND m.status = 0 AND m.del_flag = 0 " +
            "ORDER BY m.parent_id, m.order_num")
    List<Menu> selectAllRouterMenu();


    /**
     * 查询普通用户的路由信息(权限菜单)
     *
     * @param userId
     * @return
     */
    @Select("SELECT DISTINCT m.id, m.parent_id, m.menu_name, m.path, m.component, m.visible, m.status, " +
            "IFNULL(m.perms,'') AS perms, m.is_frame,  m.menu_type, m.icon, m.order_num, m.create_time " +
            "FROM sys_user_role ur " +
            "LEFT JOIN sys_role_menu rm ON ur.role_id = rm.role_id " +
            "LEFT JOIN sys_menu m ON m.id = rm.menu_id " +
            "WHERE ur.user_id = #{userId} AND m.menu_type IN ('C','M') AND m.status = 0 AND m.del_flag = 0 " +
            "ORDER BY m.parent_id, m.order_num")
    List<Menu> selectOtherRouterMenuTreeByUserId(Long userId);
}
