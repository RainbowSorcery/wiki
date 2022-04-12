package com.lyra.wiki.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LoginVO", description = "用户登录中间类")
public class LoginVO {
    private Long id;

    @Schema(name = "name", description = "用户名")
    private String name;
    @Schema(name = "loginName", description = "登录名")
    private String loginName;
    @Schema(name = "token", description = "令牌")
    private String token;

    @Override
    public String toString() {
        return "LoginVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", loginName='" + loginName + '\'' +
                ", token='" + token + '\'' +
                ", userType=" + userType +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Short userType;

    public Short getUserType() {
        return userType;
    }

    public void setUserType(Short userType) {
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
