package com.example.framework.service.Impl;

import com.example.framework.Utils.PathUtils;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.enums.AppHttpCodeEnum;
import com.example.framework.domain.exception.SystemException;
import com.example.framework.service.OssUploadService;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @Description:上传文件到OSS
 * @Author:Rainbow
 * @CreateTime:2023/12/2010:13
 */
@Service
@Data
@ConfigurationProperties(prefix = "myoss") //把OSSTest测试类的这一行注释掉，不然myoss被两个类读取会报错
//把文件上传到七牛云
public class OssUploadServiceImpl implements OssUploadService {
    //注意要从application.yml读取属性数据，下面的3个成员变量的名字必须对应application.yml的myoss属性的三个子属性名字
    private String xxaccessKey;
    private String xxsecretKey;
    private String xxbucket;


    /**
     * 上传图片文件
     *
     * @param img
     */
    @Override
    //MultipartFile是spring提供的接口
    public ResponseResult uploadImg(MultipartFile img) {
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();

        // 获取文件大小
        long fileSize = img.getSize();

        // 判断文件大小是否超过2MB（2MB=2*1024*1024 bytes）
        if (fileSize > 2 * 1024 * 1024) {
            // 抛出文件大小超过限制的异常
            throw new SystemException(AppHttpCodeEnum.FILE_SIZE_ERROR);
        }

        // 对原始文件名进行格式判断。只能上传png或jpg文件
        if (originalFilename != null && !originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")) {
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        // 生成文件路径
        String filePath = PathUtils.generateFilePath(originalFilename);
        // 上传图片到七牛云，并获取外链地址
        String url = uploadOss(img, filePath);


        return ResponseResult.okResult(url);
    }


    /**
     * 上传文件的具体代码。MultipartFile是spring提供的接口，作用是实现文件上传
     *
     * @param imgFile  图片文件流
     * @param filePath 文件存储路径
     */
    private String uploadOss(MultipartFile imgFile, String filePath) {
        // 配置七牛云存储区域和分片上传版本
        Configuration cfg = new Configuration(Region.huanan());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        UploadManager uploadManager = new UploadManager(cfg);

        //文件存储路径
        String key = filePath;

        // 创建七牛云认证对象，并获取上传凭证
        Auth auth = Auth.create(xxaccessKey, xxsecretKey);
        String upToken = auth.uploadToken(xxbucket);

        try {
            InputStream xxinputStream = imgFile.getInputStream();

            try {
                // 使用七牛云上传管理器进行文件上传
                Response response = uploadManager.put(xxinputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println("上传成功! 生成的key是: " + putRet.key);
                System.out.println("上传成功! 生成的hash是: " + putRet.hash);

                return "http://s5xzwt59m.hn-bkt.clouddn.com/" + key;

            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception e) {
            //ignore
        }
        return "上传失败";
    }
}