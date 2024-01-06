package com.example.framework.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.framework.Utils.BeanCopyUtils;
import com.example.framework.constants.SystemConstants;
import com.example.framework.domain.Link;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.vo.LinkVo;
import com.example.framework.domain.vo.PageVo;
import com.example.framework.mapper.LinkMapper;
import com.example.framework.service.LinkService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/1813:58
 */

@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的友链
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();

        //要求查的是友链表status字段的值为0，注意SystemConstants是我们写的一个常量类，用来解决字面值的书写问题
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(queryWrapper);   //存储查询的数据

        //类型转换
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);

        return ResponseResult.okResult(linkVos);
    }


    /**
     * 分页获取友联
     *
     * @param link
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageVo selectLinkPage(Link link, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(link.getName()), Link::getName, link.getName());
        queryWrapper.eq(Objects.nonNull(link.getStatus()), Link::getStatus, link.getStatus());

        Page<Link> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);

        //转换成VO
        List<Link> categories = page.getRecords();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(categories);
        return pageVo;
    }
}
