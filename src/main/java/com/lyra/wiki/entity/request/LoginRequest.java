package com.lyra.wiki.entity.request;

public class LoginRequest {
    private String loginName;
    private String password;
    private String captcha;

    @Override
    public String toString() {
        return "LoginRequest{" +
                "loginName='" + loginName + '\'' +
                ", password='" + password + '\'' +
                ", captcha='" + captcha + '\'' +
                '}';
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
