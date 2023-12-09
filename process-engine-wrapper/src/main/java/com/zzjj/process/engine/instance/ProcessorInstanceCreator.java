package com.zzjj.process.engine.instance;

import com.zzjj.process.engine.process.Processor;

/**
 * 流程节点实例化
 *
 * @author zengjin
 * @date 2023/12/09 13:49
 **/
public interface ProcessorInstanceCreator {
    /**
     * 创建实例
     *
     * @param className 类名称
     * @param name      节点id
     * @return 实例化对象
     */
    Processor newInstance(String className, String name) throws Exception;
}
