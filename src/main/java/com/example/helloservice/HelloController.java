package com.example.helloservice;

import com.example.helloservice.ratelimit.RateLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class HelloController {

    @RateLimit(100)
    @GetMapping("/hello")
    public HashMap<String, String> sayHello() {
        HashMap<String ,String> data = new HashMap<String,String>();
        data.put("msg","hello");
        return data;
    }
}
