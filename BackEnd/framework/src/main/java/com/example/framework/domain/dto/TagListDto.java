package com.example.framework.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2914:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "查询标签的请求参数dto")
public class TagListDto {

    //请求参数。用户可传可不传，这两个参数是给用户在搜索框根据name查询对应的标签，或根据remark查询对应的标签
    //标签
    private String name;

    //标签的备注
    private String remark;

}