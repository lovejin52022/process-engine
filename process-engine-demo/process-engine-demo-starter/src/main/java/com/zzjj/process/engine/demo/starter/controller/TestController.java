package com.zzjj.process.engine.demo.starter.controller;

import com.zzjj.process.engine.config.StringXmlProcessParser;
import com.zzjj.process.engine.model.ProcessContextFactory;
import com.zzjj.process.engine.process.ProcessContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zengjin
 * @date 2023/12/09 15:57
 **/
@RestController
@Slf4j
public class TestController {
    @Autowired
    private ProcessContextFactory processContextFactory;

    @GetMapping("/test1")
    public String test1() {
        ProcessContext process1 = this.processContextFactory.getContext("process1");
        process1.set("nextId", "node4");
        process1.start();

        return "true";
    }

    @GetMapping("/test2")
    public String test2() throws Exception {
        ProcessContext process1 = this.processContextFactory.getContext("process1");
        log.info("before refresh process......");
        process1.set("id", "process1");
        process1.start();
        final String dynamicXmlConfig = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<process-context " +
                "xmlns=\"http://www.w3school.com.cn\"\n" + "                 xmlns:xsi=\"http://www.w3" +
                ".org/2001/XMLSchema-instance\"\n" + "                 xsi:schemaLocation=\"http://www.w3school.com" +
                ".cn process-engine.xsd\">\n" + "    <process name=\"process1\">\n" + "        <node name=\"node1\" " +
                "class=\"com.ruyuan.process.engine.demo.springboot.processor" + ".StandardProcessorDemo\" " +
                "next=\"node2\" begin=\"true\"/>\n" + "        <node name=\"node2\" class=\"com.ruyuan.process.engine" +
                ".demo.springboot.processor" + ".StandardProcessorDemo\"/>\n" + "    </process>\n" + "\n" +
                "</process-context>";
        StringXmlProcessParser stringXmlProcessParser = new StringXmlProcessParser(dynamicXmlConfig);
        this.processContextFactory.refresh(stringXmlProcessParser.parse());
        process1 = this.processContextFactory.getContext("process1");
        log.info("after refresh process......");
        process1.set("id", "process1");
        process1.start();
        return "true";
    }
}
