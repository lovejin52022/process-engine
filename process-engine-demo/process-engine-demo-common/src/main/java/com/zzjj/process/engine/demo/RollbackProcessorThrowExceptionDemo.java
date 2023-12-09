package com.zzjj.process.engine.demo;

import com.zzjj.process.engine.process.ProcessContext;
import com.zzjj.process.engine.process.RollbackProcessor;

/**
 * @author zengjin
 * @date 2023/12/09 14:55
 **/
public class RollbackProcessorThrowExceptionDemo extends RollbackProcessor {

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println(Thread.currentThread().getName() + " - RollbackProcessorThrowExceptionDemo " + context.get("id"));
        final int i = 1 / 0;
    }

    @Override
    protected void rollback(ProcessContext context) {
        System.out.println("rollback RollbackProcessorThrowExceptionDemo " + context.get("id"));
    }
}