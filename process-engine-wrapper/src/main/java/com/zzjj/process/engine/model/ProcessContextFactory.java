package com.zzjj.process.engine.model;

import com.zzjj.process.engine.instance.ProcessorInstanceCreator;
import com.zzjj.process.engine.instance.ReflectNodeInstanceCreator;
import com.zzjj.process.engine.node.ProcessorDefinition;
import com.zzjj.process.engine.process.ProcessContext;
import com.zzjj.process.engine.store.ProcessStateStore;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zengjin
 * @date 2023/12/09 13:50
 **/
@Slf4j
public class ProcessContextFactory {
    public static final ProcessorInstanceCreator DEFAULT_INSTANCE_CREATOR = new ReflectNodeInstanceCreator();
    private List<ProcessModel> modelList;
    private final Map<String, ProcessorDefinition> processorDefinitionMap = new ConcurrentHashMap<>();
    private final ProcessorInstanceCreator instanceCreator;
    private final ProcessStateStore processStateStore;
    private final AtomicLong processInstanceCounter = new AtomicLong(0);
    private final String processEngineInstanceId;
    private final AtomicBoolean scheduleStarted = new AtomicBoolean(false);
    private final ScheduledThreadPoolExecutor scheduledThreadPool = new ScheduledThreadPoolExecutor(1);
    @Getter
    private boolean refresh = false;

    public ProcessContextFactory(List<ProcessModel> modeList) throws Exception {
        this(modeList, DEFAULT_INSTANCE_CREATOR);
    }

    public ProcessContextFactory(List<ProcessModel> modeList, ProcessorInstanceCreator instanceCreator) throws Exception {
        this(modeList, instanceCreator, null);
    }

    public ProcessContextFactory(List<ProcessModel> modeList, ProcessStateStore processStateStore) throws Exception {
        this(modeList, DEFAULT_INSTANCE_CREATOR, processStateStore);
    }

    public ProcessContextFactory(List<ProcessModel> modeList, ProcessorInstanceCreator instanceCreator,
                                 ProcessStateStore processStateStore) throws Exception {
        this.modelList = modeList;
        this.instanceCreator = instanceCreator;
        this.processStateStore = processStateStore;
        this.processEngineInstanceId = UUID.randomUUID().toString().replace("-", "");
        this.init();
    }

    private void init() throws Exception {
        // 1. 解析后的进行检查
        for (ProcessModel processModel : this.modelList) {
            processModel.check();
        }
        // 2. 构建流程定义 多个
        for (ProcessModel processModel : this.modelList) {
            ProcessorDefinition processorDefinition = processModel.build(this.instanceCreator);
            log.info("构造流程成功：\n{}", processorDefinition.toStr());
            this.processorDefinitionMap.put(processorDefinition.getName(), processorDefinition);
        }

        if (this.processStateStore != null) {
            if (this.scheduleStarted.compareAndSet(false, true)) {
                this.scheduledThreadPool.scheduleAtFixedRate(new ProcessPlaybackTask(this.processStateStore, this), 0L, 60,
                        TimeUnit.SECONDS);
            }
        }
    }

    /**
     * 构建流程上下文
     *
     * @param name 流程名称
     * @return 流程上下文
     */
    public ProcessContext getContext(String name) {
        ProcessorDefinition processorDefinition = this.processorDefinitionMap.get(name);
        if (processorDefinition == null) {
            throw new IllegalArgumentException("流程不存在");
        }
        String globalUniqueId = this.processEngineInstanceId + ":" + this.processInstanceCounter.incrementAndGet();
        return new ProcessContext(globalUniqueId, processorDefinition, this.processStateStore);
    }

    /**
     * 刷新流程
     *
     * @param modeList 解析后的流程结构
     * @throws Exception 异常
     */
    public void refresh(List<ProcessModel> modeList) throws Exception {
        synchronized (this) {
            this.modelList = modeList;
            this.refresh = false;
            this.init();
        }
    }

}
