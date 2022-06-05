package com.lyra.wiki.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.lyra.wiki.common.constant.RedisConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Controller
@Tag(name = "验证码", description = "验证码服务")
public class CaptchaController {
    private static final Logger log = LoggerFactory.getLogger(CaptchaController.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/captcha")
    @Operation(description = "生成验证码",
            summary = "生成验证码")
    public void captcha(HttpServletResponse response) {
        response.setHeader("Content-Type", "image/png:png");

        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(100, 50, 4, 4);

        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            log.error("验证码生成错误 错误信息:" + e.getMessage());
            e.printStackTrace();
        }
        String code = captcha.getCode();

        redisTemplate.opsForValue().set(RedisConstant.LOGIN_CAPTCHA_CODE + ":" + code, code, 1, TimeUnit.MINUTES);

        captcha.write(outputStream);
    }
}
