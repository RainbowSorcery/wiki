package com.lyra.wiki.entity.vo;

public class LoginVO {
    private String name;
    private String loginName;
    private String token;

    @Override
    public String toString() {
        return "LoginVO{" +
                "name='" + name + '\'' +
                ", loginName='" + loginName + '\'' +
                ", token='" + token + '\'' +
                '}';
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
