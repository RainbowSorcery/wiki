package com.lyra.wiki.interceptor;

import com.lyra.wiki.common.constant.RedisConstant;
import com.lyra.wiki.common.constant.ResponseEnums;
import com.lyra.wiki.exception.MyGraceException;
import com.lyra.wiki.utils.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class VoteInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {
        String ip = IpUtil.getRemoteIp(request);
        String contentId = request.getParameter("id");

        String result = redisTemplate.opsForValue().get(RedisConstant.REDIS_VOTE + ip + ":" + contentId);

        if (StringUtils.isNotBlank(result)) {
            throw new MyGraceException(ResponseEnums.USER_ALREADY_VOTE);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
