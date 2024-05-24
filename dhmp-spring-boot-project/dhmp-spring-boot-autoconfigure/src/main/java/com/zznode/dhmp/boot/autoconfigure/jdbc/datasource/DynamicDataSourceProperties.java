package com.zznode.dhmp.boot.autoconfigure.jdbc.datasource;

import com.zaxxer.hikari.HikariDataSource;
import com.zznode.dhmp.boot.jdbc.datasource.DataSourceAware;
import com.zznode.dhmp.jdbc.datasource.DataSourceType;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源配置项
 *
 * @author 王俊
 * @date create in 2023/5/25 15:49
 */
@ConfigurationProperties("dhmp.datasource")
public class DynamicDataSourceProperties implements BeanClassLoaderAware, InitializingBean {


    private ClassLoader classLoader;

    /**
     * 数据源定义, 请至少定义一个主数据源MASTER
     *
     * @see DataSourceType
     */
    Map<String, DataSourceDefinition> definition = new HashMap<>();
    /**
     * 数据源类型，同spring.datasource.type
     */
    private Class<? extends DataSource> type = HikariDataSource.class;
    /**
     * 是否启用密码加密
     */
    private Boolean enablePasswordEncrypt = false;

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

    public Map<String, DataSourceDefinition> getDefinition() {
        return definition;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }


    @Override
    public void afterPropertiesSet() {
        DataSourceDefinition masterDataSource = getDefinition().get(DataSourceType.MASTER);
        Assert.notNull(masterDataSource, "a master dataSource cannot be null! please check your configuration");
    }

    @Nullable
    public <D extends DataSource> D buildDataSource(Class<D> type, String dataSourceType, DataSourceDefinition definition) {
        if (!definition.getEnabled() && !DataSourceType.MASTER.equals(dataSourceType)) {
            return null;
        }
        D dataSource = DataSourceBuilder.create(getClassLoader())
                .driverClassName(definition.getDriverClassName())
                .url(definition.getUrl())
                .username(definition.getUsername())
                .password(definition.getPassword())
                .type(type)
                .build();
        if (dataSource instanceof DataSourceAware dataSourceAware) {
            dataSourceAware.setEnablePasswordEncrypt(getEnablePasswordEncrypt());
        }
        return dataSource;
    }

    public static class DataSourceDefinition {
        private Boolean enabled = false;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        /**
         * 驱动
         */
        private String driverClassName;

        /**
         * 链接地址
         */
        private String url;

        /**
         * 用户名
         */
        private String username;

        /**
         * 密码
         */
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
}
