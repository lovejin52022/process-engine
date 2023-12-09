package com.zzjj.process.engine.demo.starter.processor;

import com.zzjj.process.engine.process.ProcessContext;
import com.zzjj.process.engine.process.RollbackProcessor;
import org.springframework.stereotype.Component;

/**
 * @author zengjin
 * @date 2023/12/09 15:54
 **/
@Component
public class RollBackProcessorDemo extends RollbackProcessor {

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("RollBackProcessor " + context.get("id"));
    }

    @Override
    protected void rollback(ProcessContext context) {
        System.out.println("rollback RollBackProcessor " + context.get("id"));
    }
}