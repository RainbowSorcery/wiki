package com.lyra.wiki.common.constant;

public enum UserType {
    ADMIN_USER(Short.valueOf("0"),"管理员"),
    GENERAL_USER(Short.valueOf("1"),"普通用户");
    private Short code;
    private String message;

    UserType(Short code, String message) {
        this.code = code;
        this.message = message;
    }

    public Short getCode() {
        return code;
    }

    public void setCode(Short code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
