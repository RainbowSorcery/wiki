package com.lyra.wiki.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Resource
    private ObjectMapper objectMapper;

    @Pointcut("execution(public * com.lyra.*.controller..*Controller.*(..))")
    public void controllerPointcut() {

    }

    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        Signature signature = joinPoint.getSignature();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            log.info("-----------开始-----------");
            log.info("请求地址:{} {}", request.getRequestURI().toString(), request.getMethod());
            log.info("类名方法:{}.{}",signature.getDeclaringTypeName(), signature.getName());
            log.info("远程地址:{}", request.getRemoteAddr());
        } else {
            log.warn("获取HttpServletRequest失败.");
        }

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            System.out.println(arg);
        }

        String[] excludeProperties = {"password", "file"};

    }
}
