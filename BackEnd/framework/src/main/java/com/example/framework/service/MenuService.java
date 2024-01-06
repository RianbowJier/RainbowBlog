package com.example.framework.service;

import com.example.framework.domain.Menu;

import java.util.List;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2721:27
 */
public interface MenuService {
    /**
     * 查询用户的权限信息
     *
     * @param id
     * @return
     */
    List<String> selectPermsByUserId(Long id);


    /**
     * 查询用户的路由信息，也就是查询权限菜单
     *
     * @param userId
     * @return
     */
    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    /**
     * 查询菜单列表
     *
     * @param menu
     * @return
     */
    List<Menu> selectMenuList(Menu menu);

}
