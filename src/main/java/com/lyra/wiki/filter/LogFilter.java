package com.lyra.wiki.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class LogFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info("---------------- log开始 -----------------");
        log.info("请求地址:{} {} ", request.getRequestURI().toString(), request.getMethod());
        log.info("远程地址:{}", request.getRemoteAddr());

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        long endTime = System.currentTimeMillis();

        log.info("--------------filter结束 耗时: {}ms --------------", endTime - startTime);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
