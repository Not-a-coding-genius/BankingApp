package com.Visa.My_First_Spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @GetMapping("/ping")
    @ResponseBody
    public String ping() {
        System.out.println("Ping called");
        return "pong";
    }
}

