package com.example.framework.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2916:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditTagDto {

    private Long id;
    //标签名的备注
    private String remark;
    //标签名
    private String name;
}