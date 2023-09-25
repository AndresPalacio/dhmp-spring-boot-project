package com.zznode.dhmp.boot.autoconfigure.jdbc.datasource;

import com.zznode.dhmp.jdbc.datasource.DataSourceProvider;
import com.zznode.dhmp.jdbc.datasource.DynamicDataSource;
import com.zznode.dhmp.jdbc.datasource.DynamicDataSourceAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * 动态数据源自动配置类
 * <p>注意，数据库相关配置，建议配置在名为{application-datasource.properties}的文件中，
 * 否则请配置在主配置文件
 *
 * <p>PropertySource注解引入数据源相关配置
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
@PropertySource("${dhmp.core.config-location}application-datasource.properties")
public class DynamicDataSourceAutoConfiguration {


    @Bean
    @Primary
    DataSource dataSource(DataSourceProvider dataSourceProvider) {
        return new DynamicDataSource(dataSourceProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    DynamicDataSourceAspect dynamicDataSourceAspect() {
        return new DynamicDataSourceAspect();
    }

}
