package com.zzjj.process.engine.demo.starter.config;

import com.zzjj.process.engine.annoations.EnableProcessEngine;
import com.zzjj.process.engine.store.CacheProcessStateStore;
import com.zzjj.process.engine.store.ContinueProcessRefreshPolicy;
import com.zzjj.process.engine.store.ProcessStateStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zengjin
 * @date 2023/12/09 15:58
 **/
@EnableProcessEngine("process-demo.xml")
@Configuration
public class StoreConfig {
    @Bean
    public ProcessStateStore processStateStore() {
        return new CacheProcessStateStore(new ContinueProcessRefreshPolicy());
    }
}
