package com.lyra.wiki.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyra.wiki.common.Result;
import com.lyra.wiki.common.constant.ResponseEnums;
import com.lyra.wiki.entity.User;
import com.lyra.wiki.entity.vo.ResetUserPasswordVO;
import com.lyra.wiki.exception.MyGraceException;
import com.lyra.wiki.service.IUserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

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
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/list")
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
    public Result<Object> add(@RequestBody User user) {
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
    public Result<Object> update(@RequestBody User user) {
        User userById = userService.getById(user.getId());

        if (!Objects.equals(user.getLoginName(), userById.getLoginName())) {
            throw new MyGraceException(ResponseEnums.USERNAME_NOT_UPDATE);
        } else if (!BCrypt.checkpw(userById.getPassword(), user.getPassword()))  {
            throw new MyGraceException(ResponseEnums.PASSWORD_IS_NOT_MODIFY);
        } else {
            userService.updateById(user);
        }


        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @GetMapping("/selectById")
    public Result<User> selectById(Long id) {
        User user = userService.getById(id);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, user);
    }

    @PostMapping("/remove")
    public Result<Object> remove(Long id) {
        userService.removeById(id);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @PostMapping("/resetPassword")
    public Result<Object> resetPassword(@RequestBody ResetUserPasswordVO userPasswordVO) {
        User user = userService.getById(userPasswordVO.getId());

        if (user == null) {
            throw new MyGraceException(ResponseEnums.USER_NOT_EXITS);
        }

        if (!BCrypt.checkpw(userPasswordVO.getOldPassword(), user.getPassword())) {
            throw new MyGraceException(ResponseEnums.PASSWORD_CHECK_FILED);
        }

        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.set("password", BCrypt.hashpw(userPasswordVO.getNewPassword(), BCrypt.gensalt()));
        userUpdateWrapper.eq("id", userPasswordVO.getId());
        userService.update(userUpdateWrapper);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }
}
