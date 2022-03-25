package com.lyra.wiki.config;

import com.lyra.wiki.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/ebook/list")
                .excludePathPatterns("/category/list/tree")
                .excludePathPatterns("/ebook/getEbookByCategoryId")
                .excludePathPatterns("/doc/list/tree")
                .excludePathPatterns("/content/getContentById")
                .excludePathPatterns("/user/login");

    }
}
