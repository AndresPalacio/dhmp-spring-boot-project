package com.zznode.dhmp.boot.autoconfigure.jdbc.datasource.configurer.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zznode.dhmp.boot.autoconfigure.jdbc.datasource.DynamicDataSourceProperties;
import com.zznode.dhmp.boot.autoconfigure.jdbc.datasource.configurer.AbstractDataSourceConfigurer;

import javax.sql.DataSource;

/**
 * Hikari数据源提供
 *
 * @author 王俊
 * @date create in 2023/5/26 17:32
 */
public class HikariDataSourceConfigurer extends AbstractDataSourceConfigurer<DhmpHikariDataSource> {

    private final HikariConfig hikariConfig;

    public HikariDataSourceConfigurer(DynamicDataSourceProperties properties, HikariConfig hikariConfig) {
        super(properties);
        this.hikariConfig = hikariConfig;
    }

    @Override
    protected DataSource wrapDataSource(DhmpHikariDataSource dataSource) {
        this.hikariConfig.setDriverClassName(dataSource.getDriverClassName());
        this.hikariConfig.setJdbcUrl(dataSource.getJdbcUrl());
        this.hikariConfig.setUsername(dataSource.getUsername());
        this.hikariConfig.setPassword(dataSource.getOriginPassword());

        this.hikariConfig.copyStateTo(dataSource);
        return dataSource;
    }
}
