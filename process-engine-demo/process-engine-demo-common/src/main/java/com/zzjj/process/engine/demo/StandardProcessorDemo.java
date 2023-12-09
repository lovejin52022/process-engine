package com.zzjj.process.engine.demo;

import com.zzjj.process.engine.process.ProcessContext;
import com.zzjj.process.engine.process.StandardProcessor;

/**
 * @author zengjin
 * @date 2023/12/09 14:56
 **/
public class StandardProcessorDemo extends StandardProcessor {

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println(Thread.currentThread().getName() + " - StandProcessor " + context.get("id"));
    }
}
