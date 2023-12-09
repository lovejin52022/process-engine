package com.zzjj.process.engine.process;

/**
 * 抽象流程
 *
 * @author zengjin
 * @date 2023/12/01 23:53
 **/
public abstract class AbstractProcessor implements Processor {
    private String name;

    /**
     * 模板处理方法
     *
     * @param context 上下文
     */
    @Override
    public void process(ProcessContext context) {
        beforeProcess(context);
        processInternal(context);
        afterProcess(context);
    }

    /**
     * 流程核心逻辑
     *
     * @param context 上下文
     */
    protected abstract void processInternal(ProcessContext context);

    /**
     * 流程前操作
     *
     * @param context 上下文
     */
    private void beforeProcess(ProcessContext context) {
        // default no-op
    }

    /**
     * 流程后的操作
     *
     * @param context 上下文
     */
    private void afterProcess(ProcessContext context) {
        // default op-op
    }

    @Override
    public void caughtException(ProcessContext context, Throwable throwable) {

    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
