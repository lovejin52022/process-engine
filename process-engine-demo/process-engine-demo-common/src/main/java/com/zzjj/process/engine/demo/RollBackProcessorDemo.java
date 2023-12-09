package com.zzjj.process.engine.demo;

import com.zzjj.process.engine.process.ProcessContext;
import com.zzjj.process.engine.process.RollbackProcessor;

/**
 * @author zengjin
 * @date 2023/12/09 14:54
 **/
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
