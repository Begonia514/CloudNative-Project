package com.example.helloservice.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorType {

    TOO_MANY_REQUEST(100001, "to many request", HttpStatus.TOO_MANY_REQUESTS.value());

    @Getter
    private final int code;

    @Getter
    private final String message;

    @Getter
    private final int httpCode;

    ErrorType(int code, String message, int httpCode) {
        this.code = code;
        this.message = message;
        this.httpCode = httpCode;
    }
}
