package com.zznode.dhmp.boot.autoconfigure.mybatis;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Mybatis相关配置
 *
 * @author 王俊
 */
@Configuration
public class DhmpMybatisConfiguration {

    /**
     * <p> note：暂时只支持mysql和oracle数据库（因为只会将mysql或者oracle当作主数据源）
     * <p> 如果某个项目使用clickhouse等数据库作为主数据源，那当我没说。请自行添加配置类并使用@Primary注解覆盖此bean
     * <p> 由于mybatis在初始化SqlSessionFactory的时候就已经解析了mapper.xml文件，
     * 此时使用的是主数据源中的数据库产品信息来加载mappedStatement中的sql。
     * 无法在运行时中改变sql的绑定(暂时未找到在运行时中改变sql绑定的方法)
     *
     * @return DatabaseIdProvider
     * @see org.mybatis.spring.SqlSessionFactoryBean#setDatabaseIdProvider
     * @see org.mybatis.spring.SqlSessionFactoryBean#buildSqlSessionFactory
     * @see VendorDatabaseIdProvider#getDatabaseProductName
     */
    @Bean
    @ConditionalOnMissingBean
    public DatabaseIdProvider databaseIdProvider() {
        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties p = new Properties();
        p.setProperty("Oracle", "oracle");
        p.setProperty("MySQL", "mysql");
        databaseIdProvider.setProperties(p);
        return databaseIdProvider;
    }
}
