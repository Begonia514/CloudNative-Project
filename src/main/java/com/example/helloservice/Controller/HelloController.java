package com.example.helloservice.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.helloservice.Exception.ErrorType;
import com.example.helloservice.Exception.TooManyRequestException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
public class HelloController {

    private final Bucket bucket;
    public HelloController() {
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    @GetMapping("/hello")
    @ResponseStatus(HttpStatus.OK)
    public Object sayHello() {
        if (bucket.tryConsume(1)) {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("gid", "033");
            jsonObject.put("name", "结束乐队");
            jsonObject.put("members", "Corax, Begonia, Logician, LittleHu");
            jsonObject.put("msg", "hello");

            return jsonObject;
        } else {
            throw new TooManyRequestException(ErrorType.TOO_MANY_REQUEST);
        }
    }
}
