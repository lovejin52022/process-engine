package com.zzjj.process.engine.demo.starter.processor;

import com.zzjj.process.engine.process.ProcessContext;
import com.zzjj.process.engine.process.StandardProcessor;
import org.springframework.stereotype.Component;

/**
 * @author zengjin
 * @date 2023/12/09 15:55
 **/
@Component
public class StandardProcessorDemo2 extends StandardProcessor {

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("StandProcessor " + context.get("id"));
    }
}
