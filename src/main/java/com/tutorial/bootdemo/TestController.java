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
        log.info("Hello endpoint called");
        List<String> result = List.of("hello", "world");
        log.debug("Hello endpoint returning: {}", result);
        return result;
    }

}
