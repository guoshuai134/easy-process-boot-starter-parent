package com.shallow.universe.process.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2021\12\30 0030
 */
public class SpringContext {

    private static ApplicationContext context;

    public static void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringContext.context = context;
    }

    public static <T> T getBean(Class<T> type) {
        return context.getBean(type);
    }

    public static void autowireBean(Object bean) {
        context.getAutowireCapableBeanFactory().autowireBean(bean);
    }
}
