package com.tutorial.bootdemo.service;

import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl1 implements DemoService{
    @Override
    public void someMethod() {
        System.out.println("DemoServiceImpl1 method");
    }
}
