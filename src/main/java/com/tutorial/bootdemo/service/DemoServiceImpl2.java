package com.tutorial.bootdemo.service;

import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl2 implements DemoService {
    @Override
    public void someMethod() {
        System.out.println("DemoServiceImpl2 method");
    }
}
