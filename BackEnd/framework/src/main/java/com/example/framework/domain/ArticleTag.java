package com.example.framework.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description:新增博客文章
 * @Author:Rainbow
 * @CreateTime:2023/12/2916:22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sg_article_tag")
public class ArticleTag implements Serializable {
    private static final long serialVersionUID = 625337492348897098L;

    /**
     * 文章id
     */
    private Long articleId;
    /**
     * 标签id
     */
    private Long tagId;

}