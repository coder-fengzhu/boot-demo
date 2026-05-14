package com.tutorial.bootdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/hello")
    public List<String> hello() {
        log.info("Received request for hello endpoint.");
        List<String> response = List.of("hello", "world");
        log.info("Completed request for hello endpoint. resultCount={}", response.size());
        log.debug("Hello endpoint response={}", response);
        return response;
    }

}
