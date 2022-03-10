package com.platform.recipe.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)

public class BeanUtil implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    public static Map<String, Object> getBeansByType(Class beanClass) {
        return context.getBeansOfType(beanClass);
    }

    public static <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }
}
