package com.example.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2916:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagVo {

    private Long id;
    private String name;
    private String remark;
}