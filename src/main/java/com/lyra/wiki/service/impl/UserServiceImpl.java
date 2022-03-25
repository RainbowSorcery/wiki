package com.lyra.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyra.wiki.common.constant.ResponseEnums;
import com.lyra.wiki.entity.User;
import com.lyra.wiki.entity.request.LoginRequest;
import com.lyra.wiki.entity.vo.LoginVO;
import com.lyra.wiki.exception.MyGraceException;
import com.lyra.wiki.mapper.UserMapper;
import com.lyra.wiki.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selectByLoginName(String loginName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name", loginName);

        return userMapper.selectList(queryWrapper);
    }

    @Override
    public LoginVO login(LoginRequest loginRequest) {
        List<User> loginUserDB = this.selectByLoginName(loginRequest.getLoginName());

        if (loginUserDB == null || loginUserDB.size() == 0) {
            throw new MyGraceException(ResponseEnums.USER_NOT_EXITS);
        }

        LoginVO loginVO = new LoginVO();

        User userDB = loginUserDB.get(0);
        if (BCrypt.checkpw(loginRequest.getPassword(), userDB.getPassword()) ) {
            BeanUtils.copyProperties(userDB, loginVO);
        } else {
            throw new MyGraceException(ResponseEnums.PASSWORD_CHECK_FILED);
        }

        return loginVO;
    }
}
