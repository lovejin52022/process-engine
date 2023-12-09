package com.zzjj.process.engine.demo.starter.processor;

import com.zzjj.process.engine.process.ProcessContext;
import com.zzjj.process.engine.process.RollbackProcessor;
import org.springframework.stereotype.Component;

/**
 * @author zengjin
 * @date 2023/12/09 15:54
 **/
@Component
public class RollbackProcessorThrowExceptionDemo extends RollbackProcessor {

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("RollbackProcessorThrowExceptionDemo " + context.get("id"));
        final int i = 1 / 0;
    }

    @Override
    protected void rollback(ProcessContext context) {
        System.out.println("rollback RollbackProcessorThrowExceptionDemo " + context.get("id"));
    }
}