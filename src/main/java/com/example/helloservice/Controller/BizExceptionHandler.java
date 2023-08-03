package com.example.helloservice.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.helloservice.Exception.TooManyRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BizExceptionHandler {

    @ExceptionHandler(TooManyRequestException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public Object handleToManyRequestException(TooManyRequestException e) {
        System.err.println(e.getMessage());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "too many request");

        return jsonObject;
    }
}
