package com.lyra.wiki.service;

import com.lyra.wiki.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
