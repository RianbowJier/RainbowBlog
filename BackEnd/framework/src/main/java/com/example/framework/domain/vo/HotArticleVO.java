package com.example.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:返回给前端特定的字段，优化对象信息，减少不必要的信息传递
 * @Author:Rainbow
 * @CreateTime:2023/12/816:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
//返回给前端特定的字段
public class HotArticleVO {
    private Long id;
    //标题
    private String title;
    //访问量
    private Long viewCount;

}