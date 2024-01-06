package com.example.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.framework.domain.Category;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.vo.CategoryVo;
import com.example.framework.domain.vo.PageVo;

import java.util.List;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/816:46
 */
public interface CategoryService extends IService<Category> {
    /**
     * 查询文章分类
     *
     * @return
     */
    ResponseResult getCategoryList();

    /**
     * @return
     */
    List<CategoryVo> listAllCategory();


    /**
     * 根据类别查询文章
     *
     * @param category
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageVo selectCategoryPage(Category category, Integer pageNum, Integer pageSize);
}
