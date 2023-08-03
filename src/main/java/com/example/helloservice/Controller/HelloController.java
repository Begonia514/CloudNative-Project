package com.example.helloservice.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.helloservice.Exception.ErrorType;
import com.example.helloservice.Exception.TooManyRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    @ResponseStatus(HttpStatus.OK)
    public Object sayHello() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("gid", "033");
        jsonObject.put("name", "结束乐队");
        jsonObject.put("members", "Corax, Begonia, Logician, LittleHu");
        jsonObject.put("msg", "hello");

        return jsonObject;
    }

    @GetMapping("/error")
    public Object report() {
        throw new TooManyRequestException(ErrorType.TOO_MANY_REQUEST);
    }
}
