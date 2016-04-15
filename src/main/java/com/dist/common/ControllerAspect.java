/*
 * Copyright：成都顶呱呱投资集团有限公司 2013
 */
package com.dist.common;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.util.UserContext;

/**
 * @author heshun
 * @version V1.0, 2013-8-15
 */
@Aspect
public class ControllerAspect {
    /**
     * 获得locback对象，用于日志记录，由于该对象会被反复使用，故声明为静态变量。
     */
    static Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    /**
     * 无参构造函数
     */
    public ControllerAspect() {
        
    }

    /**
     *配置切点的空方法，切点可配置多个
     */
    @Pointcut("execution(* com.dist.controller..*.*(..))")
    public void aPointcut() {
    }

    /**
     *方法前置通知，@before注解用来表明这是前置通知。使用info()记录，不直接使用logger。
     * @param jp 目标类连接点对象
     */
    @Before("aPointcut()")
    public void beforeAdvice(JoinPoint jp) {
        //        info(jp, "before");
    }

    /**
     *方法后置通知，@AfterReturning注解用来表明这是后置通知。
     * @param jp 目标类连接点对象
     */
    @AfterReturning("aPointcut()")
    public void afterReturnAdvice(JoinPoint jp) {
        //        info(jp, "after");
        UserContext.get().popSubject();
    }

    /**
     *方法异常通知，@AfterThrowing注解用来表明这是前置通知。
     * @param jp 目标类连接点对象
     * @param e 发生的异常信息
     */
    @AfterThrowing(pointcut = "aPointcut()", throwing = "e")
    public void afterThrowAdvice(JoinPoint jp, Throwable e) {
        error(jp, e);
    }

    /**
    * 记录信息至日志，记录info级别的日志，记录类名、方法名、方法参数类型、方法参数值。
    * @param jp 连接点
    */
    private void info(JoinPoint jp, String advice) {
        String args = Arrays.toString(jp.getArgs());
        String logInfo = jp.toString() + " args:[ " + args + "]  [" + advice + "]";
        logger.info(logInfo);
    }

    /**
     * 记录异常信息至日志，记录error级别信息，记录类名、方法名、方法参数类型、方法参数值、异常信息。
     * @param jp 连接点
     * @param e 发生的异常
     */
    private void error(JoinPoint jp, Throwable e) {
        String args = Arrays.toString(jp.getArgs());

        //拼接异常信息
        String errorInfo = "[" + e + "] ";

        //拼接方法信息
        String logInfo = jp.toString() + ". args:" + args + "  errorMessage:" + errorInfo;
        logger.error(logInfo);
    }
}
