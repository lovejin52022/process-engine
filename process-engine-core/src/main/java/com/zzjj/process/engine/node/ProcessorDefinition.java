package com.zzjj.process.engine.node;

import com.zzjj.process.engine.utils.ProcessorUtils;
import lombok.Getter;

/**
 * 流程
 *
 * @author zengjin
 * @date 2023/12/01 23:41
 **/
@Getter
public class ProcessorDefinition {
    private String name;
    /**
     * 函数入口
     */
    private ProcessorNode first;

    public ProcessorDefinition() {
    }

    public ProcessorDefinition(ProcessorNode first) {
        setFirst(first);
    }

    public void setFirst(ProcessorNode first) {
        this.first = first;
        if (ProcessorUtils.hasRing(first)) {
            throw new IllegalArgumentException("Processor chain exists ring.");
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toStr() {
        StringBuilder sb = new StringBuilder();
        buildStr(sb, first);
        return sb.toString();
    }

    private static void buildStr(StringBuilder sb, ProcessorNode node) {
        for (ProcessorNode processorNode : node.getNextNodes().values()) {
            sb.append(node.getName()).append(" -> ").append(processorNode.getName()).append("\n");
            buildStr(sb, processorNode);
        }
    }

}
