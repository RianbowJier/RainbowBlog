package com.example.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:返回给前端的特定字段
 * @Author:Rainbow
 * @CreateTime:2023/12/816:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVo {

    private Long id;
    private String name;
    //描述
    private String description;

    //状态
    private String status;
}