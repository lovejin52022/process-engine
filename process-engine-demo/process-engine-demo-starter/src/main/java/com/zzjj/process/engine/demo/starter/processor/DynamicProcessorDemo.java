package com.zzjj.process.engine.demo.starter.processor;

import com.zzjj.process.engine.process.DynamicProcessor;
import com.zzjj.process.engine.process.ProcessContext;
import org.springframework.stereotype.Component;

/**
 * @author zengjin
 * @date 2023/12/09 15:52
 **/
@Component
public class DynamicProcessorDemo extends DynamicProcessor {

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("DynamicProcess " + context.get("id"));
    }

    @Override
    protected String nextNodeId(ProcessContext context) {
        return "node4";
    }
}