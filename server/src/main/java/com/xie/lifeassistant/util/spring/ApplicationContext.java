package com.xie.lifeassistant.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Comment：
 * Created by IntelliJ IDEA.
 * User: xie
 * Date: 2020/9/28 10:01
 */
@Component
public class ApplicationContext {
    private static org.springframework.context.ApplicationContext applicationContext = null;

    @Autowired
    private static Environment environment;
    public static String getPropByName(String name){
        return environment.getProperty(name);
    }

    //获取applicationContext
    public static org.springframework.context.ApplicationContext getCurrent() {
        return ApplicationContext.applicationContext;
    }

    //通过name获取 Bean.
    public static Object getBean(String name){
        return getCurrent().getBean(name);

    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz){
        return getCurrent().getBean(clazz);
    }

    public static void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) throws BeansException {
        if(ApplicationContext.applicationContext == null){
            ApplicationContext.applicationContext  = applicationContext;
        }
    }
}
