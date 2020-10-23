package com.xie.lifeassistant.util.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Comment：
 * Created by IntelliJ IDEA.
 * User: xie
 * Date: 2020/10/20 16:26
 */

/**
 * 忽略接口注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SwaggerCustomIgnore {
}