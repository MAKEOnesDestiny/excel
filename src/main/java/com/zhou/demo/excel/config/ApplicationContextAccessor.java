package com.zhou.demo.excel.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationContextAccessor implements ApplicationContextAware {

    private static ApplicationContext ac = null;

    private static boolean initialized = false;

    public static ApplicationContext getApplicationContext() {
        if (!initialized) throw new RuntimeException("application context未初始化完成");
        return ac;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
        initialized = true;
    }
}
