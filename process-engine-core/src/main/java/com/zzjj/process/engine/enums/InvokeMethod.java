package com.zzjj.process.engine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 调用方式
 *
 * @author zengjin
 * @date 2023/12/01 23:35
 **/
@Getter
@AllArgsConstructor
public enum InvokeMethod {
    SYNC("同步"),

    ASYNC("异步");

    private final String desc;
}
