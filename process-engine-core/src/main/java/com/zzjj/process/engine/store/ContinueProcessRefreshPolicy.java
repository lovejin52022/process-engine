package com.zzjj.process.engine.store;

import java.util.Map;

/**
 * @author zengjin
 * @date 2023/12/09 00:32
 **/
public class ContinueProcessRefreshPolicy implements ProcessRefreshPolicy {
    @Override
    public boolean continueExecuteProcess(String name, Map<String, String> metadata) {
        return true;
    }
}