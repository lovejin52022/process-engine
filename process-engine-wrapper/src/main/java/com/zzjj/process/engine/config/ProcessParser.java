package com.zzjj.process.engine.config;

import com.zzjj.process.engine.model.ProcessModel;

import java.util.List;

/**
 * 解析器
 *
 * @author zengjin
 * @date 2023/12/09 13:47
 **/
public interface ProcessParser {
    /**
     * 解析器
     *
     * @return 解析结果
     */
    List<ProcessModel> parse() throws Exception;
}
