package com.example.framework.handler.exception;


import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.enums.AppHttpCodeEnum;
import com.example.framework.domain.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description:全局异常处理。最终都会在这个类进行处理异常
 * @Author:Rainbow
 * @CreateTime:2023/12/1916:35
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * SystemException是我们写的类。用户登录的异常交给这里处理
     * 处理特定的SystemException异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e) {
        log.error("出现了异常! {}", e);
        return ResponseResult.errorResult(e.getCode(), e.getMsg());
    }

    /**
     * 其他异常交给这里处理
     * 处理通用的Exception异常
     *
     * @param e
     * @return
     */
    //
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e) {
        log.error("出现了异常! {}", e);
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());//枚举值是500
    }
}
