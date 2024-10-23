package com.example.blogjava.exception;

import lombok.Getter;

@Getter
public class serviceException extends RuntimeException {

    private final String code;

    public serviceException(String msg) {
        super(msg);
        this.code = "500";
    }

    public serviceException(String code, String msg) {
        super(msg);
        this.code = code;
    }

}