package com.example.helloservice;

import com.alibaba.fastjson.JSONObject;
import com.example.helloservice.Controller.BizExceptionHandler;
import com.example.helloservice.Controller.HelloController;
import com.example.helloservice.Exception.TooManyRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@SpringBootTest
class HelloServiceApplicationTests {

    HelloController helloController = null;

    private final String ans = "{\"msg\":\"hello\",\"gid\":\"033\",\"members\":\"Corax, Begonia, Logician, LittleHu\",\"name\":\"结束乐队\"}";

    @BeforeEach
    void setUp() {
        helloController = new HelloController();
    }

    void requestForResp() {
        JSONObject jsonObject = (JSONObject) helloController.sayHello();
        assert jsonObject.toJSONString().equals(ans);
    }

    void requestFor429() {
        try {
            JSONObject jsonObject = (JSONObject) helloController.sayHello();
        } catch (TooManyRequestException e){
            return;
        }
        assert false;
    }

    @Test
    void availabilityTest() throws Exception {
        requestForResp();
    }

    @Test
    void currentLimitTest() throws Exception {
        for(int i = 0; i < 10; i++) {
            requestForResp();
        }
        for(int i = 0; i < 10; i++) {
            requestFor429();
        }
    }

}
