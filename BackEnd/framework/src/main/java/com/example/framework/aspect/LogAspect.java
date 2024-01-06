package com.example.framework.aspect;

import com.alibaba.fastjson.JSON;
import com.example.framework.annotation.Systemlog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:aop切面日志
 * @Author:Rainbow
 * @CreateTime:2023/12/2017:53
 */

@Component
@Aspect    //设置当前类为切面类
@Slf4j//用于在控制台打印日志信息
public class LogAspect {
    /**
     * 设置切入点
     */
    @Pointcut("@annotation(com.example.framework.annotation.Systemlog)")
    public void pt() {

    }

    /**
     * 环绕通知
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("pt()")
    //ProceedingJoinPoint可以拿到被增强方法的信息
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //proceed方法表示调用目标方法，ret就是目标方法执行完之后的返回值
        Object ret;
        try {
            //调用下面写的'实现打印日志信息的格式信息'的方法
            handleBefore(joinPoint);

            //这是目标方法执行完成，上一行是目标方法未执行，下一行是目标方法已经执行
            ret = joinPoint.proceed();

            //调用下面写的'实现打印日志信息的数据信息'的方法
            handleAfter(ret);
        } finally {
            //下面的'实现打印日志信息的数据信息'的方法，不管有没有出异常都会被执行，确保下面的输出必然输出在控制台
            //System.lineSeparator表示当前系统的换行符
            log.info("=======================end=======================" + System.lineSeparator());
        }

        //封装成切面，然后返回
        return ret;
    }


    /**
     * 处理方法执行前的逻辑，记录请求相关信息
     *
     * @param joinPoint 切点
     */
    private void handleBefore(ProceedingJoinPoint joinPoint) {
        // 获取当前请求的属性
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        // 获取被增强方法的注解对象
        Systemlog systemlog = getSystemlog(joinPoint);

        log.info("======================Start======================");
        // 打印请求 URL
        log.info("请求URL   : {}", request.getRequestURL());
        // 打印接口描述信息
        log.info("接口描述   : {}", systemlog.businessName());
        // 打印 Http method
        log.info("请求方式   : {}", request.getMethod());
        // 打印调用 controller 的类名和方法名
        log.info("请求类名   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("访问IP    : {}", request.getRemoteHost());
        // 打印请求入参
        log.info("传入参数   : {}", JSON.toJSONString(joinPoint.getArgs()));
    }

    /**
     * 处理方法执行后的逻辑，记录返回参数
     *
     * @param ret 方法返回值
     */
    private void handleAfter(Object ret) {
        // 打印返回参数
        log.info("返回参数   : {}", JSON.toJSONString(ret));
    }

    /**
     * 获取被增强方法的注解对象
     *
     * @param joinPoint 切点
     * @return Systemlog 注解对象
     */
    private Systemlog getSystemlog(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取被增强方法的注解对象
        Systemlog systemlog = methodSignature.getMethod().getAnnotation(Systemlog.class);
        return systemlog;
    }

}
