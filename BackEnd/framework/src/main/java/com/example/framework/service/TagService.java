package com.example.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.Tag;
import com.example.framework.domain.dto.TagListDto;
import com.example.framework.domain.vo.PageVo;
import com.example.framework.domain.vo.TagVo;

import java.util.List;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2718:26
 */
public interface TagService extends IService<Tag> {
    /**
     * 查询标签列表
     *
     * @param pageNum
     * @param pageSize
     * @param tagListDto
     * @return
     */
    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    /**
     * 删除标签
     *
     * @return
     */
    ResponseResult removeById(Long id);


    /**
     * 查询所有标签
     *
     * @return
     */
    List<TagVo> listAllTag();
}