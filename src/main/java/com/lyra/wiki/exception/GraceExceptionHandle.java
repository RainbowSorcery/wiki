package com.lyra.wiki.exception;

import com.lyra.wiki.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GraceExceptionHandle {
    @ExceptionHandler(MyGraceException.class)
    public Result<Object> returnMyGraceException(MyGraceException e) {

        return new Result<>(e.getResponseEnums().getCode(), e.getResponseEnums().getMessage(), false);
    }
}
