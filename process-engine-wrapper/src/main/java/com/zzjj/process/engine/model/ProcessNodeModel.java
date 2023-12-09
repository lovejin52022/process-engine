package com.zzjj.process.engine.model;

import com.zzjj.process.engine.enums.InvokeMethod;
import lombok.Data;

/**
 * 流程函数信息
 *
 * @author zengjin
 * @date 2023/12/09 13:48
 **/
@Data
public class ProcessNodeModel {
    private String name;
    private String className;
    private String nextNode;
    private Boolean begin = false;
    private InvokeMethod invokeMethod;
}
