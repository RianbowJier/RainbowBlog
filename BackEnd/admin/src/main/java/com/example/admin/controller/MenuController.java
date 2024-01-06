package com.example.admin.controller;

import com.example.framework.Utils.BeanCopyUtils;
import com.example.framework.domain.Menu;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.vo.MenuVo;
import com.example.framework.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:菜单列表
 * @Author:Rainbow
 * @CreateTime:2023/12/2921:19
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;


    /**
     * 查询菜单
     *
     * @param menu
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Menu menu) {
        List<Menu> menus = menuService.selectMenuList(menu);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

//    /**
//     * 新增菜单
//     *
//     * @param menu
//     * @return
//     */
//    @PostMapping
//    public ResponseResult add(@RequestBody Menu menu) {
//        menuService.save(menu);
//        return ResponseResult.okResult();
//    }
}