package com.tutorial.bootdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/hello")
    public List<String> hello() {
        log.info("GET /hello - health check request");
        return List.of("hello", "world");
    }

}
