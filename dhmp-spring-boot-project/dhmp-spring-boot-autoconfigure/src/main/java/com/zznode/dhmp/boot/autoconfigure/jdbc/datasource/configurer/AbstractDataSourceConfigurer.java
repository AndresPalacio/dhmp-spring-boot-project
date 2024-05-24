package com.zznode.dhmp.boot.autoconfigure.jdbc.datasource.configurer;

import com.zaxxer.hikari.HikariDataSource;
import com.zznode.dhmp.boot.autoconfigure.jdbc.datasource.DynamicDataSourceProperties;
import com.zznode.dhmp.jdbc.datasource.DynamicDataSourceProvider;
import com.zznode.dhmp.jdbc.datasource.config.DynamicDataSourceConfigurer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.GenericTypeResolver;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据源提供抽象类
 *
 * @param <D> 数据源产品类型，hikariCp、druid、dbcp等等。
 *            <p> 默认实现了{@link HikariDataSource} 类型的数据源初始器。
 *            <p> 如需使用其他类型，请引入相关数据源类型依赖，
 *            并在配置文件中配置{@link DynamicDataSourceProperties#setType dhmp.datasource.type}
 * @author 王俊
 * @date create in 2023/5/26 17:26
 */
public abstract class AbstractDataSourceConfigurer<D extends DataSource> implements DynamicDataSourceConfigurer, InitializingBean {

    private final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();

    private final DynamicDataSourceProperties properties;

    /**
     * 数据源实现类
     */
    private final Class<D> type;

    @SuppressWarnings("unchecked")
    protected AbstractDataSourceConfigurer(DynamicDataSourceProperties properties) {
        this.properties = properties;
        this.type = (Class<D>) GenericTypeResolver.resolveTypeArgument(getClass(), AbstractDataSourceConfigurer.class);
    }

    @Override
    public void configureDataSourceProvider(DynamicDataSourceProvider dynamicDataSourceProvider) {
        this.dataSources.forEach(dynamicDataSourceProvider::addDataSource);
    }

    @Override
    public void afterPropertiesSet() {
        initialize();
    }

    /**
     * 初始化多数据源
     */
    public void initialize() {
        properties.getDefinition()
                .forEach((key, value) -> {
                    D dataSource = properties.buildDataSource(type, key, value);
                    if (dataSource != null) {
                        this.dataSources.put(key, wrapDataSource(dataSource));
                    }
                });
    }


    /**
     * 包装数据源参数
     *
     * @param dataSource 数据源
     * @return 处理后的数据源实例
     */
    protected abstract DataSource wrapDataSource(D dataSource);

}
