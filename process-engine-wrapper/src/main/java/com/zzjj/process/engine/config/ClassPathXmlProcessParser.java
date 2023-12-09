package com.zzjj.process.engine.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * @author zengjin
 * @date 2023/12/09 13:47
 **/
public class ClassPathXmlProcessParser extends XmlProcessParser {

    private final String file;

    public ClassPathXmlProcessParser(String file) {
        this.file = file.startsWith("/") ? file : "/" + file;
    }

    @Override
    protected Document getDocument() throws DocumentException {
        // 从文件加载xml
        InputStream resourceAsStream = getClass().getResourceAsStream(file);
        SAXReader saxReader = new SAXReader();
        return saxReader.read(resourceAsStream);
    }
}