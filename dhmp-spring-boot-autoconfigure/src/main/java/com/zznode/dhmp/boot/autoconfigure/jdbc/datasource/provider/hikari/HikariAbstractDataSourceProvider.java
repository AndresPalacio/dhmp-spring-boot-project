package com.zznode.dhmp.boot.autoconfigure.jdbc.datasource.provider.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zznode.dhmp.boot.autoconfigure.jdbc.datasource.DynamicDataSourceProperties;
import com.zznode.dhmp.boot.autoconfigure.jdbc.datasource.provider.AbstractDataSourceProvider;

import javax.sql.DataSource;

/**
 * Hikari数据源提供
 *
 * @author 王俊
 * @date create in 2023/5/26 17:32
 */
public class HikariAbstractDataSourceProvider extends AbstractDataSourceProvider<DhmpHikariDataSource> {

    private final HikariConfig hikariConfig;

    public HikariAbstractDataSourceProvider(DynamicDataSourceProperties properties, HikariConfig hikariConfig) {
        super(properties, DhmpHikariDataSource.class);
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
