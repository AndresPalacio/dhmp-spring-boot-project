package com.zznode.dhmp.boot.autoconfigure.jdbc.datasource.provider;

import com.zaxxer.hikari.HikariDataSource;
import com.zznode.dhmp.boot.autoconfigure.jdbc.datasource.DynamicDataSourceProperties;
import com.zznode.dhmp.jdbc.datasource.DataSourceProvider;
import com.zznode.dhmp.jdbc.datasource.DataSourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

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
public abstract class AbstractDataSourceProvider<D extends DataSource> implements DataSourceProvider {

    private final static Logger logger = LoggerFactory.getLogger(AbstractDataSourceProvider.class);

    private final Map<Object, Object> dataSources = new HashMap<>();

    private final DynamicDataSourceProperties properties;

    private final Class<D> type;

    protected AbstractDataSourceProvider(DynamicDataSourceProperties properties, Class<D> type) {
        Assert.notNull(type, "type cannot be null");
        this.properties = properties;
        this.type = type;
    }

    public Class<D> getType() {
        return type;
    }

    @Override
    public Map<Object, Object> getDataSources() {
        return dataSources;
    }

    @Override
    public DataSource getMasterDataSource() {
        Object dataSource = dataSources.get(DataSourceType.MASTER);
        if(dataSource == null){
           return init();
        }
        return (DataSource) dataSource;
    }

    /**
     * 初始化多数据源
     *
     * @return 主数据源
     */
    public D init() {

        D masterDataSource = properties.createMasterDataSource(getType());
        Assert.notNull(masterDataSource, "a master dataSource cannot be null! please check your configuration");
        dataSources.put(DataSourceType.MASTER, this.wrapDataSource(masterDataSource));
        PropertyMapper mapper = PropertyMapper.get().alwaysApplyingWhenNonNull();

        mapper.from(properties.createHguSparkDataSource(getType()))
                .as(this::wrapDataSource)
                .to((ds) -> dataSources.put(DataSourceType.HGU_SPARK, ds));

        mapper.from(properties.createStbSparkDataSource(getType()))
                .as(this::wrapDataSource)
                .to((ds) -> dataSources.put(DataSourceType.STB_SPARK, ds));

        mapper.from(properties.createHbaseDataSource(getType()))
                .as(this::wrapDataSource)
                .to((ds) -> dataSources.put(DataSourceType.HBASE, ds));

        mapper.from(properties.createClickhouseDataSource(getType()))
                .as(this::wrapDataSource)
                .to((ds) -> dataSources.put(DataSourceType.CLICKHOUSE, ds));

        return masterDataSource;
    }

    /**
     * 添加数据源类型(可能某个省份会同时使用mysql和oracle数据库)
     *
     * @param dataSourceName 数据源类型名称，不能与{@link DataSourceType}中重复。
     * @param dataSource     数据源，数据源类型必须和{@link DynamicDataSourceProperties#getType()}一致
     */
    public void addDataSource(String dataSourceName, DataSource dataSource) {
        Assert.hasText(dataSourceName, "dataSourceName cannot be null");
        Assert.notNull(dataSource, "dataSource cannot be null");
        if (this.dataSources.containsKey(dataSourceName)) {
            logger.info("already has a dataSource named with {}", dataSourceName);
            return;
        }
        if (!getType().isAssignableFrom(dataSource.getClass())) {
            logger.info("type {} is not assign with {}", dataSource.getClass().getName(), getType().getName());
            return;
        }
        this.dataSources.put(dataSourceName, dataSource);
    }

    /**
     * 包装数据源参数
     *
     * @param dataSource 数据源
     * @return 处理后的数据源实例
     */
    protected abstract DataSource wrapDataSource(D dataSource);

}
