package com.zzjj.process.engine.process;

/**
 * 回滚流程
 *
 * @author zengjin
 * @date 2023/12/09 00:00
 **/
public abstract class RollbackProcessor extends AbstractProcessor {

    /**
     * 回滚操作
     *
     * @param context 上下文
     */
    protected abstract void rollback(ProcessContext context);

}