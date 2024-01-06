package com.example.framework.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.framework.Utils.BeanCopyUtils;
import com.example.framework.constants.SystemConstants;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.Tag;
import com.example.framework.domain.dto.TagListDto;
import com.example.framework.domain.vo.PageVo;
import com.example.framework.domain.vo.TagVo;
import com.example.framework.mapper.TagMapper;
import com.example.framework.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2718:27
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    /**
     * 查询所有文章标签
     * 1、根据name或者remark查询特定标签，反之，查询所有标签
     *
     * @param pageNum
     * @param pageSize
     * @param tagListDto
     * @return
     */
    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询的条件。模糊+分页
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                // 检查 tagListDto.getName() 是否为非空。
                // 如果是，则执行比较两个参数；
                // 如果不是，则跳过这个比较操作。
                .like(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName())
                // 检查 tagListDto.getRemark() 是否为非空。
                // 如果是，则执行比较两个参数；
                // 如果不是，则跳过这个比较操作。
                .like(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());

        //分页查询。
        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum).setSize(pageSize);
        page(page, queryWrapper);

        //封装数据返回。
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    /**
     * 根据id删除标签
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult removeById(Long id) {
        // 创建一个 Wrapper 对象来添加更新条件
        LambdaUpdateWrapper<Tag> wrapper = new LambdaUpdateWrapper<>();

        wrapper.eq(Tag::getId, id)  // 根据id查询
                .eq(Tag::getDelFlag, SystemConstants.DELETE_NORMAL)  // 状态为正常
                .set(Tag::getDelFlag, SystemConstants.NON_DELETE_NORMAL);  // 设置为删除状态

        // 执行更新操作
        int rowsAffected = tagMapper.update(null, wrapper);

        // 根据受影响的行数返回响应结果
        return rowsAffected > 0 ? ResponseResult.okResult() : ResponseResult.errorResult(500, "出现异常请联系管理员");

    }


    /**
     * 查询所有标签
     *
     * @return
     */
    @Override
    public List<TagVo> listAllTag() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Tag::getId, Tag::getName);

        List<Tag> list = list(wrapper);
        
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return tagVos;
    }
}



























