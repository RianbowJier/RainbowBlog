package com.example.framework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.framework.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/1814:32
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}