package com.example.helloservice.Exception;

import lombok.Getter;

public class TooManyRequestException extends RuntimeException{

    @Getter
    private final int code;

    @Getter
    private final String message;

    @Getter
    private final int httpCode;

    public TooManyRequestException(ErrorType type) {
        this.message = type.getMessage();
        this.code = type.getCode();
        this.httpCode = type.getHttpCode();
    }
}
