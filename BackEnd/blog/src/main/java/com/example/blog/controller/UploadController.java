package com.example.blog.controller;

import com.example.framework.domain.ResponseResult;
import com.example.framework.service.OssUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Description:文件上传
 * @Author:Rainbow
 * @CreateTime:2023/12/2010:43
 */
@RestController
@Api(tags = "文件上传的相关接口文档")
public class UploadController {
    @Autowired
    private OssUploadService ossUploadService;


    /**
     * 上传文件
     *
     * @param img
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation(value = "文件上传接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "img", value = "图片文件"),
    })
    public ResponseResult uploadImg(MultipartFile img) {
        try {
            return ossUploadService.uploadImg(img);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
