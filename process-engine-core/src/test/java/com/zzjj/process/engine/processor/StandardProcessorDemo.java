package com.zzjj.process.engine.processor;

import com.zzjj.process.engine.process.ProcessContext;
import com.zzjj.process.engine.process.StandardProcessor;

/**
 * @author zengjin
 * @date 2023/12/09 00:41
 **/
public class StandardProcessorDemo extends StandardProcessor {

    private final String id;

    public StandardProcessorDemo(String id) {
        this.id = id;
    }

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("StandProcessor " + id);
    }
}