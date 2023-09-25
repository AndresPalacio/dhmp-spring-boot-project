package com.zznode.dhmp.boot.autoconfigure.jdbc.datasource;

import com.zaxxer.hikari.HikariDataSource;
import com.zznode.dhmp.boot.jdbc.datasource.DataSourceAware;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import javax.sql.DataSource;

/**
 * 数据源配置项
 *
 * @author 王俊
 * @date create in 2023/5/25 15:49
 */
@ConfigurationProperties("dhmp.datasource")
public class DynamicDataSourceProperties implements BeanClassLoaderAware, InitializingBean {


    private ClassLoader classLoader;

    private Class<? extends DataSource> type = HikariDataSource.class;

    private Boolean enablePasswordEncrypt = false;

    private Master master = new Master();
    private HguSpark hguSpark = new HguSpark();
    private StbSpark stbSpark = new StbSpark();
    private Hbase hbase = new Hbase();
    private Clickhouse clickhouse = new Clickhouse();

    public Class<? extends DataSource> getType() {
        return this.type;
    }

    public void setType(Class<? extends DataSource> type) {
        this.type = type;
    }

    public Boolean getEnablePasswordEncrypt() {
        return enablePasswordEncrypt;
    }

    public void setEnablePasswordEncrypt(Boolean enablePasswordEncrypt) {
        this.enablePasswordEncrypt = enablePasswordEncrypt;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public HguSpark getHguSpark() {
        return hguSpark;
    }

    public void setHguSpark(HguSpark hguSpark) {
        this.hguSpark = hguSpark;
    }

    public StbSpark getStbSpark() {
        return stbSpark;
    }

    public void setStbSpark(StbSpark stbSpark) {
        this.stbSpark = stbSpark;
    }

    public Hbase getHbase() {
        return hbase;
    }

    public void setHbase(Hbase hbase) {
        this.hbase = hbase;
    }

    public Clickhouse getClickhouse() {
        return clickhouse;
    }

    public void setClickhouse(Clickhouse clickhouse) {
        this.clickhouse = clickhouse;
    }

    @Override
    public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }


    @Override
    public void afterPropertiesSet() {
        Assert.notNull(getMaster(), "a master dataSource cannot be null! please check your configuration");
    }


    public DataSourceBuilder<?> initializeDataSourceBuilder(BaseDataSourceProperties properties) {
        return DataSourceBuilder.create(getClassLoader())
                .type(getType())
                .driverClassName(properties.getDriverClassName())
                .url(properties.getUrl())
                .username(properties.getUsername())
                .password(properties.getPassword());
    }

    public <T extends DataSource> T buildDataSource(Class<T> type, BaseDataSourceProperties properties) {
        T dataSource = initializeDataSourceBuilder(properties).type(type).build();
        if (dataSource instanceof DataSourceAware dataSourceAware) {
            dataSourceAware.setEnablePasswordEncrypt(getEnablePasswordEncrypt());
        }
        return dataSource;
    }


    public <T extends DataSource> T createMasterDataSource(Class<T> type) {
        Master master = getMaster();
        Assert.notNull(master, "you must provide a master datasource profile.");
        return buildDataSource(type, master);

    }

    public <T extends DataSource> T createHguSparkDataSource(Class<T> type) {
        HguSpark hguSpark = getHguSpark();
        if (!hguSpark.getEnabled()) {
            return null;
        }
        return buildDataSource(type, hguSpark);
    }

    public <T extends DataSource> T createStbSparkDataSource(Class<T> type) {
        StbSpark stbSpark = getStbSpark();
        if (!stbSpark.getEnabled()) {
            return null;
        }
        return buildDataSource(type, stbSpark);
    }

    public <T extends DataSource> T createHbaseDataSource(Class<T> type) {
        Hbase hbase = getHbase();
        if (!hbase.getEnabled()) {
            return null;
        }
        return buildDataSource(type, hbase);
    }

    public <T extends DataSource> T createClickhouseDataSource(Class<T> type) {
        Clickhouse clickhouse = getClickhouse();
        if (!clickhouse.getEnabled()) {
            return null;
        }
        return buildDataSource(type, clickhouse);
    }

    public static class BaseDataSourceProperties {
        private Boolean enabled = false;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        private String driverClassName;

        private String url;

        private String username;

        private String password;

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class Master extends BaseDataSourceProperties {

    }

    public static class HguSpark extends BaseDataSourceProperties {

    }

    public static class StbSpark extends BaseDataSourceProperties {

    }

    public static class Hbase extends BaseDataSourceProperties {

    }

    public static class Clickhouse extends BaseDataSourceProperties {

    }
}
