package com.lyra.wiki.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@Schema(name = "User", description = "用户")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Schema(name = "id", description = "用户id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 登陆名
     */
    @Schema(name = "loginName", description = "登录名")
    private String loginName;

    /**
     * 昵称
     */
    @Schema(name = "name", description = "用户名")
    private String name;

    /**
     * 密码
     */
    @Schema(name = "password", description = "密码")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", loginName=" + loginName +
            ", name=" + name +
            ", password=" + password +
        "}";
    }
}
