package com.zzjj.process.engine.node;

import com.zzjj.process.engine.process.ProcessContext;
import com.zzjj.process.engine.processor.DynamicProcessorDemo;
import com.zzjj.process.engine.processor.RollBackProcessorDemo;
import com.zzjj.process.engine.processor.StandardProcessorDemo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author zengjin
 * @date 2023/12/09 00:36
 **/
public class ProcessorDefinitionTest {

    @Test
    public void testHasRing() {
        ProcessorNode nodeA = new ProcessorNode();
        ProcessorNode nodeB = new ProcessorNode();
        ProcessorNode nodeC = new ProcessorNode();
        ProcessorNode nodeD = new ProcessorNode();
        ProcessorNode nodeE = new ProcessorNode();
        nodeA.setName("A");
        nodeB.setName("B");
        nodeC.setName("C");
        nodeD.setName("D");
        nodeE.setName("E");
        nodeA.addNextNode(nodeB);
        nodeB.addNextNode(nodeC);
        nodeC.addNextNode(nodeA);
        assertThrows(IllegalArgumentException.class, () -> new ProcessorDefinition(nodeA));
    }

    @Test
    public void testProcessContext() {
        ProcessorNode nodeA = new ProcessorNode();
        ProcessorNode nodeB = new ProcessorNode();
        ProcessorNode nodeC = new ProcessorNode();
        ProcessorNode nodeD = new ProcessorNode();
        ProcessorNode nodeE = new ProcessorNode();
        nodeA.setName("A");
        nodeA.setProcessor(new StandardProcessorDemo("A"));

        nodeB.setName("B");
        nodeB.setProcessor(new RollBackProcessorDemo("B", false));

        nodeC.setName("C");
        nodeC.setProcessor(new DynamicProcessorDemo("C", "D"));

        nodeD.setName("D");
        nodeD.setProcessor(new RollBackProcessorDemo("D", true));

        nodeE.setName("E");
        nodeE.setProcessor(new StandardProcessorDemo("E"));

        nodeA.addNextNode(nodeB);
        nodeB.addNextNode(nodeC);
        nodeC.addNextNode(nodeD);
        nodeC.addNextNode(nodeE);

        ProcessorDefinition processorDefinition = new ProcessorDefinition(nodeA);
        ProcessContext context = new ProcessContext("1", processorDefinition);
        context.start();
    }
}
