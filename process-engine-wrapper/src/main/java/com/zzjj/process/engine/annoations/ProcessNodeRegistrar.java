package com.zzjj.process.engine.annoations;

import com.zzjj.process.engine.config.ClassPathXmlProcessParser;
import com.zzjj.process.engine.model.ProcessContextFactory;
import com.zzjj.process.engine.model.ProcessModel;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * 注解流程入口注入
 *
 * @author zengjin
 * @date 2023/12/09 13:45
 **/
public class ProcessNodeRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        try {
            String configFile = (String) annotationMetadata.getAnnotationAttributes(EnableProcessEngine.class.getName())
                    .get("value");
            // 1. 解析得到流程
            ClassPathXmlProcessParser classPathXmlProcessParser = new ClassPathXmlProcessParser(configFile);
            List<ProcessModel> processLists = classPathXmlProcessParser.parse();

            // 2. 注册ProcessContextFactory到beanDefine
            BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(ProcessContextFactory.class);
            bdb.addConstructorArgValue(new ArrayList<>(processLists));
            bdb.addConstructorArgReference("springBeanInstanceCreator");
            bdb.addConstructorArgReference("processStateStore");
            beanDefinitionRegistry.registerBeanDefinition(ProcessContextFactory.class.getName(), bdb.getBeanDefinition());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
