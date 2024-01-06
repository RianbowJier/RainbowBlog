package com.example.framework.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/1814:37
 */
@Data
@Accessors(chain = true)
public class UserInfoVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    private String sex;

    private String email;

}