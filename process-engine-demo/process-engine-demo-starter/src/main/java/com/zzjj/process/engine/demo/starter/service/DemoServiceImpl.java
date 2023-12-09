package com.zzjj.process.engine.demo.starter.service;

import org.springframework.stereotype.Service;

/**
 * @author zengjin
 * @date 2023/12/09 15:53
 **/
@Service
public class DemoServiceImpl implements DemoService {
    @Override
    public void sayHello() {
        System.out.println("hello");
    }
}