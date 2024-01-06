package com.example.framework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/821:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVo {
    //数据集合
    private List rows;
    //数据总数
    private Long total;

}