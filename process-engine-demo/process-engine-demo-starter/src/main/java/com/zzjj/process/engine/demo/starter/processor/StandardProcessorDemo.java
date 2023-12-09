package com.zzjj.process.engine.demo.starter.processor;

import com.zzjj.process.engine.demo.starter.service.DemoService;
import com.zzjj.process.engine.process.ProcessContext;
import com.zzjj.process.engine.process.StandardProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zengjin
 * @date 2023/12/09 15:55
 **/
@Component
public class StandardProcessorDemo extends StandardProcessor {

    @Autowired
    private DemoService demoService;

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("StandProcessor " + context.get("id"));
        this.demoService.sayHello();
    }
}
