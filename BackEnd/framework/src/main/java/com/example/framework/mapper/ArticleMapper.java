package com.example.framework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.framework.domain.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 查询所有文章
     *
     * @param page
     * @return
     */
    @Select("SELECT * FROM sg_article " +
            "WHERE sg_article.status = '0' AND sg_article.del_flag= '0'" +
            "ORDER BY sg_article.view_count DESC")
    List<Article> articleList(Page<Article> page);
}
