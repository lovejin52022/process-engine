package com.zzjj.process.engine.model;

import com.zzjj.process.engine.instance.ProcessorInstanceCreator;
import com.zzjj.process.engine.node.ProcessorDefinition;
import com.zzjj.process.engine.node.ProcessorNode;
import com.zzjj.process.engine.process.Processor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 解析流程信息
 *
 * @author zengjin
 * @date 2023/12/09 13:48
 **/
@Data
public class ProcessModel {
    public String name;
    public Map<String, ProcessNodeModel> nodes = new HashMap<>();

    /**
     * 添加一个节点
     *
     * @param processNodeModel 节点配置
     */
    public void addNode(ProcessNodeModel processNodeModel) {
        if (nodes.containsKey(processNodeModel.getName())) {
            throw new IllegalArgumentException("同一个流程不能定义多个相同id的节点");
        }
        nodes.put(processNodeModel.getName(), processNodeModel);
    }

    /**
     * 检查配置是否合法：
     * 1、className是否合法
     * 2、上下节点依赖是否正确。
     * 3、是否仅有一个开始节点
     */
    public void check() {
        int startNode = 0;
        for (ProcessNodeModel processNodeModel : nodes.values()) {
            String className = processNodeModel.getClassName();
            try {
                Class.forName(className);
            } catch (Throwable e) {
                throw new IllegalArgumentException("无法加载节点[" + processNodeModel.getName() + "]的类：" + className);
            }
            String nextNode = processNodeModel.getNextNode();
            if (nextNode != null) {
                String[] nextNodes = nextNode.split(",");
                for (String nodeName : nextNodes) {
                    if (!nodes.containsKey(nodeName)) {
                        throw new IllegalArgumentException("节点[name=" + nodeName + "]不存在");
                    }
                }
            }
            if (processNodeModel.getBegin()) {
                startNode++;
            }
        }
        if (startNode != 1) {
            throw new IllegalArgumentException("不合法的流程，每个流程只能有一个开始节点");
        }
    }

    /**
     * 构建一个流程的定义 定义名称 入口first->next
     *
     * @param creator 流程实列
     * @return 流程定义
     * @throws Exception 错误
     */
    public ProcessorDefinition build(ProcessorInstanceCreator creator) throws Exception {
        Map<String, ProcessorNode> processorNodeMap = new HashMap<>();
        ProcessorDefinition processorDefinition = new ProcessorDefinition();
        processorDefinition.setName(name);
        // 第一次循环，将所有的processNodeModel转化为processorNode实例，并保存在集合中，并将第一个节点放入processorDefinition
        for (ProcessNodeModel processNodeModel : nodes.values()) {
            String className = processNodeModel.getClassName();
            Processor processor = creator.newInstance(className, processNodeModel.getName());
            ProcessorNode processorNode = new ProcessorNode();
            processorNode.setProcessor(processor);
            processorNode.setName(processNodeModel.getName());
            if (processNodeModel.getBegin()) {
                processorDefinition.setFirst(processorNode);
            }
            processorNode.setInvokeMethod(processNodeModel.getInvokeMethod());
            processorNodeMap.put(processNodeModel.getName(), processorNode);
        }

        // 第二次循环，将所有节点建立关联关系
        for (ProcessorNode processNode : processorNodeMap.values()) {
            String nextNodeStr = nodes.get(processNode.getName()).getNextNode();
            if (nextNodeStr == null) {
                continue;
            }
            String[] nextNodes = nextNodeStr.split(",");
            for (String nextNode : nextNodes) {
                processNode.addNextNode(processorNodeMap.get(nextNode));
            }
        }
        return processorDefinition;
    }
}
