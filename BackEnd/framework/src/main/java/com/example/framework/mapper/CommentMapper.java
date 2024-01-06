package com.example.framework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.framework.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/1917:50
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
