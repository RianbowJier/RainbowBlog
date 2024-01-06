package com.example.blog.controller;

import com.example.framework.Utils.BeanCopyUtils;
import com.example.framework.annotation.Systemlog;
import com.example.framework.constants.SystemConstants;
import com.example.framework.domain.Comment;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.dto.AddCommentDto;
import com.example.framework.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/1917:56
 */
@RestController
@RequestMapping("/comment")
@Api(tags = "评论的相关接口文档")
public class CommentController {

    @Autowired
    private CommentService commentService;


    /**
     * 获取文章评论列表
     *
     * @param articleId：文章id
     * @param pageNum：当前页数
     * @param pageSize：获取当前页大小
     * @return
     */
    @GetMapping("commentList")
    @Systemlog(businessName = "获取文章评论列表接口")
    @ApiOperation(value = "获取文章评论列表接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页号"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小"),
            @ApiImplicitParam(name = "articleId)", value = "文章id")
    })
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT, articleId, pageNum, pageSize);
    }


    /**
     * 添加评论
     *
     * @param addCommentDto 添加评论实体类
     * @return
     */
    @PostMapping
    @Systemlog(businessName = "添加评论接口")
    @ApiOperation(value = "添加评论接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addCommentDto", value = "添加评论实体类")
    })
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto) {
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }


    /**
     * 获取友链接的评论
     *
     * @param pageNum
     * @param pageSize
     * @return
     */

    @GetMapping("/linkCommentList")
    @Systemlog(businessName = "获取友链接的评论接口")
    @ApiOperation(value = "获取友链接的评论接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页号"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小")
    })
    //在友链的评论区发送评论。ResponseResult是我们在framework工程写的类
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize) {
        return commentService.commentList(SystemConstants.LINK_COMMENT, null, pageNum, pageSize);
    }
}









