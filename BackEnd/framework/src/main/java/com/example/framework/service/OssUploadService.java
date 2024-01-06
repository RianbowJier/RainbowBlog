package com.example.framework.service;

import com.example.framework.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2010:12
 */
public interface OssUploadService {
    //图片上传到七牛云
    ResponseResult uploadImg(MultipartFile img) throws IOException;
}