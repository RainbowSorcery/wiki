package com.lyra.wiki.config;

import com.lyra.wiki.interceptor.AdminInterceptor;
import com.lyra.wiki.interceptor.UserInterceptor;
import com.lyra.wiki.interceptor.VoteInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private AdminInterceptor adminInterceptor;

    @Autowired
    private VoteInterceptor voteInterceptor;

    @Autowired
    private UserInterceptor userInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(voteInterceptor)
                .addPathPatterns("/doc/increaseVoteCount/**");

        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/collect/**")
                .addPathPatterns("/user/logout/**")
                .excludePathPatterns("/captcha");

        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-ui/**")
                .excludePathPatterns("/bus/v3/api-docs/**")
                .excludePathPatterns("/v3/api-docs/**")
                .excludePathPatterns("/ebook/list")
                .excludePathPatterns("/category/list/tree")
                .excludePathPatterns("/ebook/getEbookByCategoryId")
                .excludePathPatterns("/doc/list/tree")
                .excludePathPatterns("/content/getContentById")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/logout/**")
                .excludePathPatterns("/doc/increaseVoteCount/**")
                .excludePathPatterns("/captcha")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/collect/**");

    }
}
