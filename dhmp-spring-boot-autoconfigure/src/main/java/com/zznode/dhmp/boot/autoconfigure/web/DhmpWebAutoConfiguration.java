package com.zznode.dhmp.boot.autoconfigure.web;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 描述
 *
 * @author 王俊
 * @date create in 2023/8/25
 */

@AutoConfiguration
@EnableConfigurationProperties(DhmpWebProperties.class)
public class DhmpWebAutoConfiguration {

}
