package com.example.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:自定义注解
 * @Author:Rainbow
 * @CreateTime:2023/12/2017:51
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Systemlog {
    String businessName();
}
