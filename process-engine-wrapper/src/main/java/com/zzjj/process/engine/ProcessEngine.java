package com.zzjj.process.engine;

import com.zzjj.process.engine.config.ProcessParser;
import com.zzjj.process.engine.model.ProcessContextFactory;
import com.zzjj.process.engine.process.ProcessContext;
import com.zzjj.process.engine.store.ProcessStateStore;
import lombok.extern.slf4j.Slf4j;

/**
 * 流程引擎
 *
 * @author zengjin
 * @date 2023/12/09 15:03
 **/
@Slf4j
public class ProcessEngine {
    private final ProcessContextFactory factory;

    public ProcessEngine(ProcessParser processParser) throws Exception {
        this(processParser, null);
    }

    public ProcessEngine(ProcessParser processParser, ProcessStateStore processStateStore) throws Exception {
        factory = new ProcessContextFactory(processParser.parse(), processStateStore);
    }

    /**
     * 获取一个流程上下文
     *
     * @param name 流程名称
     * @return 流程上下文
     */
    public ProcessContext getContext(String name) {
        return factory.getContext(name);
    }
}
