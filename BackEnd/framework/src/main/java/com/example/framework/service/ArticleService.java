package com.example.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.framework.domain.Article;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.dto.AddArticleDto;
import com.example.framework.domain.dto.ArticleDto;
import com.example.framework.domain.vo.ArticleByIdVo;
import com.example.framework.domain.vo.PageVo;

public interface ArticleService extends IService<Article> {

    /**
     * 获取热门文章，存储到列表
     */
    ResponseResult hotArticleList();


    /**
     * 分类查询文章列表
     *
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    /**
     * 获取文章详细信息
     *
     * @param id
     * @return
     */
    ResponseResult getArticleDetail(Long id);


    /**
     * 根据文章id从mysql查询文章
     *
     * @param id
     * @return
     */
    ResponseResult updateRedisViewCount(Long id);

    /**
     * 发布文章
     *
     * @param article
     * @return
     */
    ResponseResult add(AddArticleDto article);

    /**
     * 根据类别查询文章基本信息
     *
     * @param article
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize);

    /**
     * 根据id查询文章信息
     *
     * @param id
     * @return
     */
    ArticleByIdVo getInfo(Long id);

    /**
     * 修改文章信息
     *
     * @param article
     */
    void edit(ArticleDto article);
}