package com.example.framework.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2915:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddTagDto {
    //标签名
    private String name;
    //标签的备注
    private String remark;

}