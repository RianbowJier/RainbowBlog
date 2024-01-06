package com.example.framework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.framework.domain.Article;
import com.example.framework.domain.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/816:45
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {


    /**
     * 根据类别查询文章
     *
     * @param categoryId
     * @param page
     * @return
     */
    @Select("SELECT * FROM sg_article " +
            "LEFT JOIN sg_category ON sg_article.category_id = sg_category.id " +
            "WHERE sg_category.id = #{categoryId} AND sg_article.status = '0' AND sg_article.del_flag = '0'" +
            "ORDER BY sg_article.view_count DESC")
    List<Article> articleListByCategory(@Param("categoryId") Long categoryId, Page<Article> page);
}
