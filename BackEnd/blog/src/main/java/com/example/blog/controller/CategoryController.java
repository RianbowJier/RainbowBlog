package com.example.blog.controller;

import com.example.framework.annotation.Systemlog;
import com.example.framework.domain.ResponseResult;
import com.example.framework.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/816:58
 */

@RestController
@RequestMapping("/category")
@Api(tags = "文章分类的相关接口文档")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    @Systemlog(businessName = "获取分类列表接口")
    @ApiOperation(value = "获取分类列表接口")
    public ResponseResult getCategoryList() {
        return categoryService.getCategoryList();
    }
}
