package com.zznode.dhmp.boot.autoconfigure.jdbc.datasource;

import com.zznode.dhmp.jdbc.datasource.DynamicDataSource;
import com.zznode.dhmp.jdbc.datasource.annotation.EnableDynamicDataSource;
import com.zznode.dhmp.jdbc.datasource.annotation.EnableDynamicDataSourceAspect;
import com.zznode.dhmp.jdbc.datasource.config.DelegatingDynamicDataSourceConfiguration;
import com.zznode.dhmp.jdbc.datasource.interceptor.AbstractDynamicDataSourceConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 动态数据源自动配置类
 * <p>注意，数据库相关配置，建议配置在名为application-datasource.properties的文件中
 *
 * @author 王俊
 * @date create in 2023/5/24 18:03
 */
@AutoConfiguration(before = {DataSourceAutoConfiguration.class})
@ConditionalOnClass({DynamicDataSource.class})
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@Import({
        DynamicDataSourceConfiguration.Hikari.class,
        DynamicDataSourceConfiguration.Druid.class,
        DynamicDataSourceConfiguration.Dbcp2.class
})
public class DynamicDataSourceAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @EnableDynamicDataSource
    @ConditionalOnMissingBean(DelegatingDynamicDataSourceConfiguration.class)
    static class EnableDynamicDataSourceConfiguration {

    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(AbstractDynamicDataSourceConfiguration.class)
    public static class EnableDynamicDataSourceAspectConfiguration {

        @Configuration(proxyBeanMethods = false)
        @EnableDynamicDataSourceAspect()
        @ConditionalOnProperty(prefix = "spring.aop", name = "proxy-target-class", havingValue = "false")
        public static class JdkDynamicAutoProxyConfiguration {

        }

        @Configuration(proxyBeanMethods = false)
        @EnableDynamicDataSourceAspect(proxyTargetClass = true)
        @ConditionalOnProperty(prefix = "spring.aop", name = "proxy-target-class", havingValue = "true",
                matchIfMissing = true)
        public static class CglibAutoProxyConfiguration {

        }

    }

}
