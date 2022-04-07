package com.lyra.wiki.common;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Result", description = "统一返回类")
public class Result<T> {
    @Schema(name = "statusCode", description = "状态码")
    private Integer statusCode;
    @Schema(name = "message", description = "信息")
    private String message;
    @Schema(name = "success", description = "是否成功")
    private Boolean success;
    private T data;

    public Result(Integer statusCode, String message, Boolean success) {
        this.statusCode = statusCode;
        this.message = message;
        this.success = success;
    }

    public Result(Integer statusCode, String message, Boolean success, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
