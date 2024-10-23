package com.example.blogjava.exception;

import com.example.blogjava.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(serviceException.class)
    @ResponseBody
    public Result serviceExcetption(serviceException e){
        return Result.error(e.getCode(),e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result globalException(Exception e) {
        e.printStackTrace();
        return Result.error("500", "系统错误");
    }

}
