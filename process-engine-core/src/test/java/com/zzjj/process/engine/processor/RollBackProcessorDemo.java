package com.zzjj.process.engine.processor;

import com.zzjj.process.engine.process.ProcessContext;
import com.zzjj.process.engine.process.RollbackProcessor;

/**
 * @author zengjin
 * @date 2023/12/09 00:40
 **/
public class RollBackProcessorDemo extends RollbackProcessor {

    private final String id;
    private boolean throwException = false;

    public RollBackProcessorDemo(String id, boolean throwException) {
        this.throwException = throwException;
        this.id = id;
    }

    @Override
    protected void processInternal(ProcessContext context) {
        System.out.println("RollBackProcessor " + id);
        if (throwException) {
            final int i = 1 / 0;
        }
    }

    @Override
    protected void rollback(ProcessContext context) {
        System.out.println("rollback RollBackProcessor " + id);
    }
}
