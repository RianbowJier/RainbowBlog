package com.example.admin.controller;

import com.example.framework.annotation.Systemlog;
import com.example.framework.domain.Article;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.dto.AddArticleDto;
import com.example.framework.domain.dto.ArticleDto;
import com.example.framework.domain.vo.ArticleByIdVo;
import com.example.framework.domain.vo.PageVo;
import com.example.framework.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2916:24
 */
@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 新增文章
     *
     * @param article
     * @return
     */
    @PostMapping
    @Systemlog(businessName = "新增博客文章")
    public ResponseResult add(@RequestBody AddArticleDto article) {
        return articleService.add(article);
    }


    /**
     * 查询文章基本信息
     *
     * @param article
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Article article, Integer pageNum, Integer pageSize) {
        PageVo pageVo = articleService.selectArticlePage(article, pageNum, pageSize);
        return ResponseResult.okResult(pageVo);
    }


    /**
     * 修改文章基本信息
     * 1、先查询根据文章id查询对应的文章
     * 2、然后修改文章
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id") Long id) {
        ArticleByIdVo article = articleService.getInfo(id);
        return ResponseResult.okResult(article);
    }

    @PutMapping
    //②
    public ResponseResult edit(@RequestBody ArticleDto article) {
        articleService.edit(article);
        return ResponseResult.okResult();
    }


    /**
     * 删除文章
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id) {
        articleService.removeById(id);
        return ResponseResult.okResult();
    }
}












