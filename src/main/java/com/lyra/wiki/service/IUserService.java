package com.lyra.wiki.service;

import com.lyra.wiki.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyra.wiki.entity.request.LoginRequest;
import com.lyra.wiki.entity.vo.LoginVO;

import java.util.List;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
public interface IUserService extends IService<User> {

    List<User> selectByLoginName(String loginName);

    LoginVO login(LoginRequest loginRequest);
}
