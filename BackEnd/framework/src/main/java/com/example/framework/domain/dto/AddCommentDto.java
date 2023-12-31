package com.example.framework.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2711:33
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "添加评论的实体类")
public class AddCommentDto {

    @ApiModelProperty(notes = "评论类型 (0代表文章评论，1代表友链评论 )")
    private Long id;

    //评论类型（0代表文章评论，1代表友链评论）
    private String type;
    //文章id
    private Long articleId;
    //根评论id
    private Long rootId;
    //评论内容
    private String content;
    //所回复的目标评论的userid
    private Long toCommentUserId;
    //回复目标评论id
    private Long toCommentId;

    //由于我们在MyMetaObjectHandler类配置了mybatisplus的字段自动填充
    //所以就能指定哪个字段在什么时候进行自动填充，例如该类其它字段新增了数据，那么createBy字段就会自动填充值
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    //由于我们在MyMetaObjectHandler类配置了mybatisplus的字段自动填充
    //所以就能指定哪个字段在什么时候进行自动填充，例如该类其它字段新增了数据，那么createTime字段就会自动填充值
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //由于我们在MyMetaObjectHandler类配置了mybatisplus的字段自动填充
    //所以就能指定哪个字段在什么时候进行自动填充，例如该类其它字段新增或更新了数据，那么createBy字段就会自动填充值
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    //由于我们在MyMetaObjectHandler类配置了mybatisplus的字段自动填充
    //所以就能指定哪个字段在什么时候进行自动填充，例如该类其它字段新增或更新了数据，那么updateTime字段就会自动填充值
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
}
