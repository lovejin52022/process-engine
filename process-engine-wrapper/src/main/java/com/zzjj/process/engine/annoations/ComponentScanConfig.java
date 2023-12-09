package com.zzjj.process.engine.annoations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zengjin
 * @date 2023/12/09 13:43
 **/
@Configuration
@ComponentScan(value = {"com.zzjj.process.engine.instance"})
public class ComponentScanConfig {
}
