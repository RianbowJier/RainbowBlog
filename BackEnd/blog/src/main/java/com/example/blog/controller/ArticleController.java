package com.example.blog.controller;


import com.example.framework.annotation.Systemlog;
import com.example.framework.domain.ResponseResult;
import com.example.framework.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author admin
 */
@RestController
@RequestMapping("/article")
@Api(tags = "文章的相关接口文档")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

//    @GetMapping("/list")
//    //Article是公共模块的实体类
//    public List<Article> test() {
//        //查询数据库的所有数据
//        return articleService.list();
//    }


    /**
     * 获取热门文章列表
     *
     * @return
     */
    @GetMapping("hotArticleList")
    @Systemlog(businessName = "热门文章列表接口")
    @ApiOperation(value = "获取所有热门文章接口")
    public ResponseResult hotArticleList() {
        ResponseResult responseResult = articleService.hotArticleList();

        return responseResult;
    }


    /**
     * 获取所有文章或者根据分类获取文章列表
     *
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @GetMapping("/articleList")
    @Systemlog(businessName = "文章列表接口")
    @ApiOperation(value = "获取所有文章接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页号"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小"),
            @ApiImplicitParam(name = "categoryId)", value = "文章类别id")
    })
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    /**
     * 获取文章详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Systemlog(businessName = "获取文章详情接口")
    @ApiOperation(value = "获取文章详情接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章id"),
    })
    public ResponseResult getArticleDetail(@PathVariable("id") Long id) {
        //根据id查询文章详情
        return articleService.getArticleDetail(id);

    }

    /**
     * @param id
     * @return
     */
    @PutMapping("/updateViewCount/{id}")
    @Systemlog(businessName = "根据文章id从mysql查询文章接口")
    @ApiOperation(value = "更新文章浏览量接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id)", value = "文章id")
    })
    public ResponseResult updateViewCount(@PathVariable("id") Long id) {
        return articleService.updateRedisViewCount(id);
    }

}












