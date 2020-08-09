package com.zhou.demo.excel.spring;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerComposite
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ExcelBeanArguementResolver());
    }
}
