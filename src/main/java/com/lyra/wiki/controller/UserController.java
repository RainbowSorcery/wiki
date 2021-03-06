package com.lyra.wiki.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyra.wiki.common.Result;
import com.lyra.wiki.common.constant.RedisConstant;
import com.lyra.wiki.common.constant.ResponseEnums;
import com.lyra.wiki.common.constant.UserType;
import com.lyra.wiki.entity.User;
import com.lyra.wiki.entity.request.LoginRequest;
import com.lyra.wiki.entity.vo.ForgetPasswordVO;
import com.lyra.wiki.entity.vo.LoginVO;
import com.lyra.wiki.entity.vo.ResetUserPasswordVO;
import com.lyra.wiki.exception.MyGraceException;
import com.lyra.wiki.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户", description = "用户接口")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String LOGIN_CACHE = "LOGIN_CACHE:";

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/list")
    @Operation(description = "分页搜索用户列表",
            summary = "分页搜索用户列表",
            parameters = {
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "pageSize", description = "每页条数"),
            @Parameter(name = "condition", description = "条件")
    })
    public Result<Page<User>> list(Integer current, Integer pageSize, String condition) {
        if (current == null) {
            current = 0;
        }

        if (pageSize == null) {
            pageSize = 10;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(condition)) {
            queryWrapper.like("name", condition);
        }

        Page<User> userPage = userService.page(new Page<>(current, pageSize), queryWrapper);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, userPage);
    }

    @PostMapping("/add")
    @Operation(description = "添加管理员",
            summary = "添加管理员")
    public Result<Object> add(@RequestBody User user) {

        // 设置用户状态为管理员
        user.setUserType(UserType.ADMIN_USER.getCode());
        List<User> users = userService.selectByLoginName(user.getLoginName());

        if (users != null && users.size() > 0) {
            throw new MyGraceException(ResponseEnums.USER_FOUND);
        }

        // 对密码进行加密
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userService.save(user);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @PostMapping("/update")
    @Operation(description = "更新用户",
            summary = "更新用户")
    public Result<Object> update(@RequestBody User user) {
        User userById = userService.getById(user.getId());

        // 当密码设置为空时，MyBatis Plus不会对密码字段进行更新操作，当进行用户信息更新时 避免更新时对密码进行更新
        user.setPassword(null);
        user.setLoginName(null);

        if (userById == null) {
            throw new MyGraceException(ResponseEnums.USER_NOT_EXITS);
        }

        userService.updateById(user);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @GetMapping("/selectById")
    @Operation(description = "根据id查询用户",
            summary = "根据id查询用户",
            parameters = {@Parameter(name = "id", description = "用户id")})
    public Result<User> selectById(Long id) {
        User user = userService.getById(id);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, user);
    }

    @PostMapping("/remove")
    @Operation(description = "根据id删除用户",
            summary = "根据id删除用户",
            parameters = {@Parameter(name = "id", description = "用户id")})
    public Result<Object> remove(Long id) {
        userService.removeById(id);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @PostMapping("/resetPassword")
    @Operation(description = "重置用户密码",
            summary = "重置用户密码")
    public Result<Object> resetPassword(@RequestBody ResetUserPasswordVO userPasswordVO) {
        User user = userService.getById(userPasswordVO.getId());

        if (user == null) {
            throw new MyGraceException(ResponseEnums.USER_NOT_EXITS);
        }

        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.set("password", BCrypt.hashpw(userPasswordVO.getNewPassword(), BCrypt.gensalt()));
        userUpdateWrapper.eq("id", userPasswordVO.getId());
        userService.update(userUpdateWrapper);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @PostMapping("/forgetPassword")
    public Result<Object> forgetPassword(@RequestBody ForgetPasswordVO forgetPasswordVO) {
        User user = userService.getOne(new QueryWrapper<User>().eq("login_name", forgetPasswordVO.getLoginName()));
        if (user == null) {
            return new Result<>(ResponseEnums.USER_NOT_EXITS.getCode(), ResponseEnums.USER_NOT_EXITS.getMessage(), false);
        }


        if (!BCrypt.checkpw(forgetPasswordVO.getOldPassword(), user.getPassword())) {
            throw new MyGraceException(ResponseEnums.PASSWORD_CHECK_FILED);
        }

        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.set("password", BCrypt.hashpw(forgetPasswordVO.getNewPassword(), BCrypt.gensalt()));
        userUpdateWrapper.eq("id", user.getId());
        userService.update(user, userUpdateWrapper);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @PostMapping("/login")
    @Operation(description = "登录",
            summary = "登录")
    public Result<LoginVO> login(@RequestBody LoginRequest loginRequest) {

        LoginVO loginVO = userService.login(loginRequest);
        String token = UUID.randomUUID().toString();
        loginVO.setToken(token);

        try {
            redisTemplate.opsForValue().set(LOGIN_CACHE + token, objectMapper.writeValueAsString(loginVO), 3600 * 24, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true,loginVO);
    }

    @Operation(description = "退出登录",
            summary = "退出登录")
    @PostMapping("/logout/{token}")
    public Result<Object> logout(@PathVariable String token) {
        redisTemplate.delete(LOGIN_CACHE + token);
        log.debug("退出登录成功:{}", token);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @PostMapping("/register")
    public Result<Object> register(@RequestBody User user) {
        List<User> users = userService.selectByLoginName(user.getLoginName());

        if (users != null && users.size() > 0) {
            throw new MyGraceException(ResponseEnums.USER_FOUND);
        }

        user.setUserType(UserType.GENERAL_USER.getCode());
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userService.save(user);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }
}
