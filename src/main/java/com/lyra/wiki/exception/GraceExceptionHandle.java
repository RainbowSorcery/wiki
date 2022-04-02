package com.lyra.wiki.exception;

import com.lyra.wiki.common.Result;
import com.lyra.wiki.common.constant.ResponseEnums;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GraceExceptionHandle {
    @ExceptionHandler(MyGraceException.class)
    public Result<Object> returnMyGraceException(MyGraceException e) {

        return new Result<>(e.getResponseEnums().getCode(), e.getResponseEnums().getMessage(), false);
    }

    @ExceptionHandler({SizeLimitExceededException.class})
    public Result<Object> returnFileSizeException() {

        return new Result<>(ResponseEnums.FILE_UPLOAD_MAX_SIZE.getCode(), ResponseEnums.FILE_UPLOAD_MAX_SIZE.getMessage(), false);
    }
}
