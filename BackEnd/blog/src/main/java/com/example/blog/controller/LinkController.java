package com.example.blog.controller;

import com.example.framework.annotation.Systemlog;
import com.example.framework.domain.ResponseResult;
import com.example.framework.service.LinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/1814:01
 */
@RestController
@RequestMapping("link")
@Api(tags = "博客友情链接的相关接口文档")
public class LinkController {

    @Autowired
    private LinkService linkService;


    /**
     * 获取所有友情链接
     */
    @GetMapping("/getAllLink")
    @Systemlog(businessName = "获取所有友情链接")
    @ApiOperation(value = "获取所有友情链接接口")
    public ResponseResult getAllLink() {
        return linkService.getAllLink();
    }


}














