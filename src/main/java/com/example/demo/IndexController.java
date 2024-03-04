package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/welcome")
public class IndexController {
    @GetMapping("/hello")
    public String display() {
        return "Hello World!...This is Arun";
    }
    @GetMapping("/hi")
    public String displayHi() {
        return "Hello World!...un";
    }
}
