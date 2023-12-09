package com.zzjj.process.engine.config;

import com.zzjj.process.engine.enums.InvokeMethod;
import com.zzjj.process.engine.model.ProcessModel;
import com.zzjj.process.engine.model.ProcessNodeModel;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengjin
 * @date 2023/12/09 13:58
 **/
public abstract class XmlProcessParser implements ProcessParser {

    @Override
    public List<ProcessModel> parse() throws Exception {
        Document document = getDocument();
        Element rootElement = document.getRootElement();
        List<Element> processElements = rootElement.elements();
        List<ProcessModel> processModels = new ArrayList<>();
        // 多个处理流程
        for (Element process : processElements) {
            // 解析流程
            ProcessModel model = new ProcessModel();
            model.setName(process.attributeValue("name"));
            List<Element> elements = process.elements();
            for (Element node : elements) {

                // 解析流程各节点信息
                ProcessNodeModel processNodeModel = new ProcessNodeModel();
                processNodeModel.setName(node.attributeValue("name"));
                processNodeModel.setClassName(node.attributeValue("class"));
                String next = node.attributeValue("next");
                if (next != null) {
                    processNodeModel.setNextNode(next);
                }
                String begin = node.attributeValue("begin");
                processNodeModel.setBegin(Boolean.parseBoolean(begin));
                String invokeMethodStr = node.attributeValue("invoke-method");
                InvokeMethod invokeMethod = invokeMethodStr == null ? InvokeMethod.SYNC :
                        InvokeMethod.valueOf(invokeMethodStr.toUpperCase());
                processNodeModel.setInvokeMethod(invokeMethod);
                model.addNode(processNodeModel);
            }
            processModels.add(model);
        }
        return processModels;
    }

    protected abstract Document getDocument() throws DocumentException;

}