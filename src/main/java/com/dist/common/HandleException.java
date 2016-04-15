package com.dist.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标识controller的方法，不需要使用系统提供的异常处理。
 * 
 * @author ShenYuTing
 * @version V1.0, 2013-8-12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleException {
}

