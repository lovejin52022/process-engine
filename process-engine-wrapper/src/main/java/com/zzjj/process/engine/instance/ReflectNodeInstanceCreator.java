package com.zzjj.process.engine.instance;

import com.zzjj.process.engine.process.Processor;

/**
 * 反射创建实例
 *
 * @author zengjin
 * @date 2023/12/09 13:52
 **/
public class ReflectNodeInstanceCreator implements ProcessorInstanceCreator {

    @Override
    public Processor newInstance(String className, String name) throws Exception {
        Class<?> clazz = Class.forName(className);
        Object o = clazz.newInstance();
        if (!(o instanceof Processor)) {
            throw new IllegalArgumentException("类" + className + "不是Processor实例");
        }
        Processor processor = (Processor) o;
        processor.setName(name);
        return (Processor) o;
    }
}