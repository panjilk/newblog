package com.example.blogjava.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    public static final String SUCCESS = "102";
    public static final String ERROR = "404";
    private String code;
    private String msg;
    private T data;

    public static <T> Result<T> success(T data){
        return new Result<>(SUCCESS,"success",data);
    }
    public static <T> Result<T> success(){
        return new Result<>(SUCCESS,"success",null);
    }
    public static <T> Result<T> error(String msg){
        return new Result<>(ERROR,msg,null);
    }

    public static <T> Result<T> error(String code,String msg){
        return new Result<>(code,msg,null);
    }

}
