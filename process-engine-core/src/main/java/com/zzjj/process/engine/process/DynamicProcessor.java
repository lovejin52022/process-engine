package com.zzjj.process.engine.process;

/**
 * 动态选择节点
 *
 * @author zengjin
 * @date 2023/12/01 23:59
 **/
public abstract class DynamicProcessor extends AbstractProcessor {

    /**
     * 获取下一个节点的id
     *
     * @param context 上下文
     * @return 下一个节点的id
     */
    protected abstract String nextNodeId(ProcessContext context);

}