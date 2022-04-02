package com.lyra.wiki.common.constant;

public enum ResponseEnums {
    OK(200, "成功"),
    FILE_UPLOAD_SUCCESS(201, "文件上传成功"),
    FILED(500, "失败"),
    USER_FOUND(501, "用户名已存在"),
    USERNAME_NOT_UPDATE(502, "用户名不允许修改"),
    PASSWORD_IS_NOT_MODIFY(503, "密码不允许直接修改,请点击重置密码按钮"),
    USER_NOT_EXITS(504, "用户不存在"),
    PASSWORD_CHECK_FILED(505, "密码校验失败"),
    USER_ALREADY_VOTE(506, "该用户已经点过赞了"),
    FILE_UPLOAD_FILED(507, "文件上传失败"),
    FILE_UPLOAD_MAX_SIZE(508, "文件上传失败,文件大小限制10MB");
    private Integer code;
    private String message;

    ResponseEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
