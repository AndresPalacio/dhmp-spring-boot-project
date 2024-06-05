package com.zznode.dhmp.boot.autoconfigure.data.process.sensitive;

import com.zznode.dhmp.data.process.sensitive.SensitiveDataProcessor;
import com.zznode.dhmp.data.process.sensitive.annotation.EnableSensitiveAspect;
import com.zznode.dhmp.data.process.sensitive.config.AbstractSensitiveConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 敏感数据处理自动配置类
 *
 * @author 王俊
 */
@AutoConfiguration
@ConditionalOnClass(SensitiveDataProcessor.class)
@Import(SensitiveDataProcessorConfiguration.class)
@EnableConfigurationProperties(SensitiveProperties.class)
public class SensitiveDataProcessAutoConfiguration {


    @ConditionalOnMissingBean(AbstractSensitiveConfiguration.class)
    @Configuration(proxyBeanMethods = false)
    public static class EnableSensitiveAspectConfiguration {

        @Configuration(proxyBeanMethods = false)
        @EnableSensitiveAspect()
        @ConditionalOnProperty(prefix = "spring.aop", name = "proxy-target-class", havingValue = "false")
        public static class JdkDynamicAutoProxyConfiguration {

        }

        @Configuration(proxyBeanMethods = false)
        @EnableSensitiveAspect(proxyTargetClass = true)
        @ConditionalOnProperty(prefix = "spring.aop", name = "proxy-target-class", havingValue = "true",
                matchIfMissing = true)
        public static class CglibAutoProxyConfiguration {

        }

    }
}
