package com.zzjj.process.engine.store;

import java.util.Map;

/**
 * 流程刷新策略
 *
 * @author zengjin
 * @date 2023/12/09 00:04
 **/
public interface ProcessRefreshPolicy {
    /**
     * 是否继续执行流程
     *
     * @param name     流程名称
     * @param metadata 流程元数据
     * @return 是否继续执行流程
     */
    boolean continueExecuteProcess(String name, Map<String, String> metadata);
}
