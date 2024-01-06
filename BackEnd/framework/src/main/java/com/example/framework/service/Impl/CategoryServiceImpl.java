package com.example.framework.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.framework.Utils.BeanCopyUtils;
import com.example.framework.constants.SystemConstants;
import com.example.framework.domain.Article;
import com.example.framework.domain.Category;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.vo.CategoryVo;
import com.example.framework.domain.vo.PageVo;
import com.example.framework.mapper.CategoryMapper;
import com.example.framework.service.ArticleService;
import com.example.framework.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/816:46
 */

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    /**
     * 获取文章分类列表
     *
     * @return
     */
    @Override
    public ResponseResult getCategoryList() {
        // 创建查询条件包装器，筛选文章状态为正常的文章
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);

        // 查询文章列表
        List<Article> articleList = articleService.list(articleWrapper);

        // 使用流处理获取文章的分类id，并去重
        Set<Long> categoryIds = articleList.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());

        // 查询分类表，并筛选出状态为正常的分类
        List<Category> categories = listByIds(categoryIds).stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());

        // 将结果转换为CategoryVo实体列表返回给前端
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        // 返回封装后的响应结果
        return ResponseResult.okResult(categoryVos);
    }

    /**
     * 查询文章类别
     *
     * @return
     */
    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, SystemConstants.NORMAL);

        List<Category> list = list(wrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);

        return categoryVos;
    }


    /**
     * 根据类别查询文章
     *
     * @param category
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageVo selectCategoryPage(Category category, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        // 优雅处理名称和状态查询条件
        queryWrapper
                .like(StringUtils.hasText(category.getName()), Category::getName, category.getName())
                .eq(Objects.nonNull(category.getStatus()), Category::getStatus, category.getStatus());
        //分页查询
        Page<Category> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        // 执行分页查询
        page(page, queryWrapper);

        // 转换为VO对象
        List<Category> categories = page.getRecords();

        // 创建并返回PageVo对象
        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(BeanCopyUtils.copyBeanList(categories, CategoryVo.class)); // 假设这里需要转换成CategoryVo类

        return pageVo;
    }
}











