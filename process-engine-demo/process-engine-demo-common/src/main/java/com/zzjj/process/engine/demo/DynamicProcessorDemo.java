package com.zzjj.process.engine.demo;

import com.zzjj.process.engine.process.DynamicProcessor;
import com.zzjj.process.engine.process.ProcessContext;

/**
 * @author zengjin
 * @date 2023/12/09 14:54
 **/
public class DynamicProcessorDemo extends DynamicProcessor {

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("DynamicProcess " + context.get("id"));
    }

    @Override
    protected String nextNodeId(ProcessContext context) {
        return context.get("nextId");
    }
}

