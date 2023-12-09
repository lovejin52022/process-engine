package com.zzjj.process.engine.model;

import com.alibaba.fastjson.JSONObject;
import com.zzjj.process.engine.process.ProcessContext;
import com.zzjj.process.engine.store.ProcessStateStore;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zengjin
 * @date 2023/12/09 14:20
 **/
@Slf4j
public class ProcessPlaybackTask implements Runnable {
    private final ProcessContextFactory processContextFactory;
    private final ProcessStateStore processStateStore;

    public ProcessPlaybackTask(ProcessStateStore processStateStore, ProcessContextFactory processContextFactory) {
        this.processStateStore = processStateStore;
        this.processContextFactory = processContextFactory;
    }

    @Override
    public void run() {
        Collection<String> keys = processStateStore.pollUnCompletedProcess(5, TimeUnit.MINUTES,
                processContextFactory.isRefresh());
        if (keys == null || keys.isEmpty()) {
            return;
        }
        for (String key : keys) {
            // 尝试对该任务进行加锁，避免分布式多实例对同一个流程进行重放
            boolean lock = processStateStore.lock(key);
            if (!lock) {
                continue;
            }
            try {
                Map<String, String> metadata = processStateStore.getMap(key);
                String state = metadata.get(ProcessContext.META_CURRENT_EXECUTE_STATE_KEY);
                String processName = metadata.get(ProcessContext.META_PROCESS_NAME_KEY);
                String currentNode = metadata.get(ProcessContext.META_CURRENT_EXECUTE_NODE_KEY);
                ProcessContext context = processContextFactory.getContext(processName);
                for (Map.Entry<String, String> entry : metadata.entrySet()) {
                    if (!entry.getKey().startsWith(ProcessContext.META_PROCESS_PARAM_KEY)) {
                        continue;
                    }
                    String paramsKey = entry.getKey();
                    String[] split = paramsKey.split("#");
                    String paramKey = split[1];
                    String paramClassName = split[2];
                    String value = entry.getValue();
                    Class<?> clazz;
                    try {
                        clazz = Class.forName(paramClassName);
                    } catch (Exception e) {
                        try {
                            clazz = Class.forName(getInnerClassName(paramClassName));
                        } catch (Exception e1) {
                            log.error("反序列化流程参数失败：", e1);
                            return;
                        }
                    }
                    Object o = JSONObject.parseObject(value, clazz);
                    context.set(paramKey, o);
                }
                if (ProcessContext.EXECUTE_STATE_INVOKE.equals(state)) {
                    // 流程在执行阶段中断了
                    log.info("发现一个被中断的流程，开始重新执行流程，name={}, 从节点 {} 继续流程，参数：{}", processName, currentNode,
                            JSONObject.toJSON(context.params()));
                    context.start(currentNode, key);
                } else if (ProcessContext.EXECUTE_STATE_ROLLBACK.equals(state)) {
                    // 流程在回滚阶段中断了
                    log.info("发现一个回滚过程中断的流程，开始重新执行回滚，name={}, 从节点 {} 开始回滚", processName, currentNode);
                    context.rollbackFrom(key, currentNode);
                }
            } finally {
                processStateStore.unlock(key);
            }
        }
    }

    private static String getInnerClassName(String className) {
        String[] split = className.split("\\.");
        String[] temp = new String[split.length - 1];
        System.arraycopy(split, 0, temp, 0, temp.length);
        String join = String.join(".", temp);
        return join + "$" + split[split.length - 1];
    }
}
