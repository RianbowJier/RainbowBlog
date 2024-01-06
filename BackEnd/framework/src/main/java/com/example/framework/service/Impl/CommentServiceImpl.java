package com.example.framework.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.framework.Utils.BeanCopyUtils;
import com.example.framework.constants.SystemConstants;
import com.example.framework.domain.Comment;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.enums.AppHttpCodeEnum;
import com.example.framework.domain.exception.SystemException;
import com.example.framework.domain.vo.CommentVo;
import com.example.framework.domain.vo.PageVo;
import com.example.framework.mapper.CommentMapper;
import com.example.framework.service.CommentService;
import com.example.framework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 35238
 * @date 2023/7/24 0024 23:08
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    /**
     * 获取文章评论列表
     *
     * @param articleId：文章id
     * @param pageNum：当前页数
     * @param pageSize：获取当前页大小
     * @return
     */
    @Override
    //
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

        //对articleId进行判断，作用是得到指定的文章。如果是文章评论，才会判断articleId，避免友链评论判断articleId时出现空指针
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType), Comment::getArticleId, articleId)
                .eq(Comment::getRootId, SystemConstants.COMMENT_ROOT)  //查询根评论（rootID为-1表示根评论）
                .eq(Comment::getType, commentType);  //评论类型

        // 分页查询整个评论区的每一条评论
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        // 将查询到的评论按时间倒序排列
        List<Comment> sortedComments = page.getRecords().stream()
                .sorted(Comparator.comparing(Comment::getCreateTime).reversed())
                .collect(Collectors.toList());

        // 将排序后的评论列表转换为前端需要的CommentVo列表
        List<CommentVo> commentVoList = convertToCommentList(sortedComments);

        // 遍历评论列表，查询子评论并补充到CommentVo的children字段
        for (CommentVo commentVo : commentVoList) {
            // 查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            // 将查询到的子评论集合赋值给CommentVo类的children字段
            commentVo.setChildren(children);
        }

        // 将处理后的评论列表返回给前端
        return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
    }


    /**
     * 在文章的评论区发表评论
     *
     * @param comment
     */
    @Override
    public ResponseResult addComment(Comment comment) {
        //注意前端在调用这个发送评论接口时，在请求体是没有向我们传入createTime、createId、updateTime、updateID字段，所以
        //我们这里往后端插入数据时，就会导致上面那行的四个字段没有值
        //为了解决这个问题，我们在framework工程新增了MyMetaObjectHandler类、修改了Comment类。详细可自己定位去看一下代码
        //限制用户在发送评论时，评论内容不能为空。如果为空就抛出异常
        if (!StringUtils.hasText(comment.getContent())) {
            //AppHttpCodeEnum是我们写的枚举类，CONTENT_NOT_NULL代表提示''
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }

        //解决了四个字段没有值的情况，就可以直接调用mybatisplus提供的save方法往数据库插入数据(用户发送的评论的各个字段)了
        save(comment);

        //封装响应返回
        return ResponseResult.okResult();
    }


    /**
     * 根据根评论的id，查询对应的所有子评论（只查到二级评论，不再往深查）
     *
     * @param id
     * @return
     */
    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Comment::getRootId, id)
                .orderByDesc(Comment::getCreateTime);  // 对子评论按照时间进行排序

        List<Comment> comments = list(queryWrapper);
        // 将查询到的子评论列表转换为前端需要的CommentVo列表
        List<CommentVo> commentVos = convertToCommentList(comments);
        return commentVos;
    }

    //

    /**
     * 将评论列表转换为前端需要的CommentVo列表
     * TODO：联合查询优化查询效率
     *
     * @param list
     * @return
     */
    private List<CommentVo> convertToCommentList(List<Comment> list) {
        // 获取评论区的所有评论
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);

        // 遍历评论列表，查询用户名并补充CommentVo中的相关字段
        for (CommentVo commentVo : commentVos) {
            // 根据commentVo类中的createBy字段，查询user表中的nickname字段（子评论的用户昵称）
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            // 将查询到的昵称赋值给commentVo类的username字段
            commentVo.setUsername(nickName);

            // 查询根评论的用户昵称
            if (commentVo.getToCommentUserId() != -1) {
                // 判断如果getToCommentUserId为1，表示这条评论是根评论
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                // 将查询到的根评论用户昵称赋值给commentVo类的toCommentUserName字段
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        // 返回给前端的评论列表
        return commentVos;
    }


}
