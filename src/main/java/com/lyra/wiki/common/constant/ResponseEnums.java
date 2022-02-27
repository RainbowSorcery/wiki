package com.lyra.wiki.common.constant;

public enum ResponseEnums {
    OK(200, "成功"),
    FILED(500, "失败");
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
