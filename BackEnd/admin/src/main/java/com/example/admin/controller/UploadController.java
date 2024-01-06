package com.example.admin.controller;

import com.example.framework.domain.ResponseResult;
import com.example.framework.service.OssUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description:文件上传接口
 * @Author:Rainbow
 * @CreateTime:2023/12/2916:18
 */
@RestController
public class UploadController {

    @Autowired
    private OssUploadService uploadService;


    /**
     * 上传文件
     *
     * @param multipartFile
     * @return
     */
    @PostMapping("/upload")
    public ResponseResult uploadImg(@RequestParam("img") MultipartFile multipartFile) {
        try {
            return uploadService.uploadImg(multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传上传失败");
        }
    }
}