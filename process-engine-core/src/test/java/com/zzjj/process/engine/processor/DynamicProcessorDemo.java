package com.zzjj.process.engine.processor;

import com.zzjj.process.engine.process.DynamicProcessor;
import com.zzjj.process.engine.process.ProcessContext;

/**
 * @author zengjin
 * @date 2023/12/09 00:39
 **/
public class DynamicProcessorDemo extends DynamicProcessor {

    private final String id;
    private final String nextId;

    public DynamicProcessorDemo(String id, String nextId) {
        this.id = id;
        this.nextId = nextId;
    }

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("DynamicProcess " + id);
    }

    @Override
    protected String nextNodeId(ProcessContext context) {
        return nextId;
    }
}
