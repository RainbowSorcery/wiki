package com.lyra.wiki.exception;

import com.lyra.wiki.common.constant.ResponseEnums;

public class MyGraceException extends RuntimeException {
    private ResponseEnums responseEnums;

    public MyGraceException(ResponseEnums responseEnums) {
        super("状态码:" + responseEnums.getCode() + "\t" + "异常信息:" + responseEnums.getMessage());
        this.responseEnums = responseEnums;
    }

    public ResponseEnums getResponseEnums() {
        return responseEnums;
    }

    public void setResponseEnums(ResponseEnums responseEnums) {
        this.responseEnums = responseEnums;
    }
}
