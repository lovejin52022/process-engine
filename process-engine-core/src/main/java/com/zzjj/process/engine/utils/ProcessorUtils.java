package com.zzjj.process.engine.utils;

import com.zzjj.process.engine.node.ProcessorNode;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 流程工具类
 *
 * @author zengjin
 * @date 2023/12/01 23:42
 **/
public class ProcessorUtils {

    /**
     * 默认线程池
     */
    private static final ExecutorService DEFAULT_POOL = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60, TimeUnit.MICROSECONDS, new SynchronousQueue<>(), new ProcessorThreadFactory());

    public static class ProcessorThreadFactory implements ThreadFactory {

        private static int threadInitNumber = 0;

        private static synchronized int nextThreadNum() {
            return threadInitNumber++;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "processor-engine-" + nextThreadNum());
        }
    }

    /**
     * 是否循环调用
     *
     * @param node 初始函数入口
     * @return boolean
     */
    public static boolean hasRing(ProcessorNode node) {
        return hasRing(node, new HashSet<>());
    }

    /**
     * 是否循环调用
     *
     * @param node  函数节点
     * @param idSet 路径集合
     * @return boolean
     */
    private static boolean hasRing(ProcessorNode node, Set<String> idSet) {
        Map<String, ProcessorNode> nextNodes = node.getNextNodes();
        if (nextNodes == null || nextNodes.isEmpty()) {
            return false;
        } else {
            idSet.add(node.getName());
            boolean ret = false;
            for (Map.Entry<String, ProcessorNode> entry : nextNodes.entrySet()) {
                ProcessorNode value = entry.getValue();
                if (idSet.contains(value.getName())) {
                    return true;
                } else {
                    idSet.add(value.getName());
                    ret = ret || hasRing(value, new HashSet<>(idSet));
                }
            }
            return ret;
        }
    }

    public static void executeAsync(Runnable runnable) {
        DEFAULT_POOL.execute(runnable);
    }
}
