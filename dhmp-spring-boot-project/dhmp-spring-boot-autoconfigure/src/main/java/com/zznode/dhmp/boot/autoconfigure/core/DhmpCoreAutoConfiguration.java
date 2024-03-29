package com.zznode.dhmp.boot.autoconfigure.core;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 自动配置类
 *
 * @author 王俊
 * @date create in 2023/5/24 15:52
 */
@AutoConfiguration
@EnableConfigurationProperties(CoreProperties.class)
public class DhmpCoreAutoConfiguration {

}
