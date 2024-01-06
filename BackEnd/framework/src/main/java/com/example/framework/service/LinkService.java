package com.example.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.framework.domain.Link;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.vo.PageVo;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/1813:57
 */
public interface LinkService extends IService<Link> {
    //查询友链
    ResponseResult getAllLink();


    /**
     * 分页查询友链
     *
     * @param link
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageVo selectLinkPage(Link link, Integer pageNum, Integer pageSize);
}
