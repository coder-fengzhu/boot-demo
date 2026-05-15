package com.tutorial.bootdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/hello")
    public List<String> hello(@RequestParam(required = false) Long a) {
        System.out.println("hello...");
        log.info("abc: {}", a);
        return List.of("hello", "world");
    }

}
