package com.example.framework.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.framework.constants.SystemConstants;
import com.example.framework.domain.Menu;
import com.example.framework.mapper.MenuMapper;
import com.example.framework.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2721:27
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Override
    //查询用户的权限信息
    public List<String> selectPermsByUserId(Long id) {
        //根据用户id查询用户的权限信息。用户id为1代表管理员，如果是管理员就返回所有的权限
        if (id == 1L) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            //查询条件是permissions中需要有所有菜单类型为C或者F的权限。
            wrapper.in(Menu::getMenuType, SystemConstants.TYPE_MENU, SystemConstants.TYPE_BUTTON);

            //查询条件是permissions中需要有状态为正常的权限。
            wrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);

            //查询条件是permissions中需要未被删除的权限的权限
            List<Menu> menus = list(wrapper);
            return menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
        }

        //如果不是管理员就返回对应用户所具有的权限
        return getBaseMapper().selectPermsByOtherUserId(id);
    }

    /**
     * 查询用户的路由信息，也就是查询权限菜单
     *
     * @param userId
     * @return
     */
    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {

        MenuMapper menuMapper = getBaseMapper();

        List<Menu> menus = null;

        //判断是否是超级管理员，用户id为id代表超级管理员，如果是就返回所有符合要求的权限菜单
        if (userId.equals(1L)) {
            menus = menuMapper.selectAllRouterMenu();
        } else {
            //如果不是超级管理员，就查询对应用户所具有的路由信息(权限菜单)
            menus = menuMapper.selectOtherRouterMenuTreeByUserId(userId);
        }

        //构建成tree，也就是子父菜单树，有层级关系
        //思路:先找出第一层的菜单，然后再找子菜单(也就是第二层)，把子菜单的结果赋值给Menu类的children字段
        List<Menu> menuTree = xxbuildMenuTree(menus, 0L);

        return menuTree;
    }

    /**
     * 用于把List集合里面的数据构建成tree，也就是子父菜单树，有层级关系
     *
     * @param menus
     * @param parentId
     * @return
     */
    private List<Menu> xxbuildMenuTree(List<Menu> menus, long parentId) {
        List<Menu> menuTree = menus.stream()
                //过滤找出父菜单树，也就是第一层
                .filter(menu -> menu.getParentId().equals(parentId))
                //xxgetChildren是我们在下面写的方法，用于获取子菜单的List集合
                .map(menu -> menu.setChildren(xxgetChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 用于获取传入参数的子菜单，并封装为List集合返回
     *
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> xxgetChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                //通过过滤得到子菜单
                .filter(m -> m.getParentId().equals(menu.getId()))
                //如果有三层菜单的话，也就是子菜单的子菜单，我们就用下面那行递归(自己调用自己)来处理
                .map(m -> m.setChildren(xxgetChildren(m, menus)))
                .collect(Collectors.toList());
        return childrenList;
    }


    /**
     * 查询菜单列表
     *
     * @param menu
     * @return
     */
    @Override
    public List<Menu> selectMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        //menuName模糊查询
        queryWrapper.like(StringUtils.hasText(menu.getMenuName()), Menu::getMenuName, menu.getMenuName())
                .eq(StringUtils.hasText(menu.getStatus()), Menu::getStatus, menu.getStatus())
                .orderByAsc(Menu::getParentId, Menu::getOrderNum);  //排序 parent_id和order_num

        List<Menu> menus = list(queryWrapper);

        return menus;
    }

    @Override
    public boolean save(Menu entity) {
        return super.save(entity);
    }
}