package com.zzjj.process.engine.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.StringReader;

/**
 * @author zengjin
 * @date 2023/12/09 14:00
 **/
public class StringXmlProcessParser extends XmlProcessParser {

    private final String xmlConfig;

    public StringXmlProcessParser(String xmlConfig) {
        this.xmlConfig = xmlConfig;
    }

    @Override
    protected Document getDocument() throws DocumentException {
        // 解析xml配置信息
        SAXReader saxReader = new SAXReader();
        StringReader reader = new StringReader(xmlConfig);
        return saxReader.read(reader);
    }
}