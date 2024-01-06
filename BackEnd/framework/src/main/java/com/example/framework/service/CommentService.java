package com.example.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.framework.domain.Comment;
import com.example.framework.domain.ResponseResult;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/1917:50
 */
public interface CommentService extends IService<Comment> {

    /**
     * 获取文章评论
     *
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    /**
     * 在文章的评论区发送评论
     *
     * @param comment
     * @return
     */
    ResponseResult addComment(Comment comment);
}