package com.zznode.dhmp.boot.autoconfigure.mybatis.flex.customizer;

import com.mybatisflex.core.mybatis.FlexConfiguration;
import com.mybatisflex.spring.boot.ConfigurationCustomizer;
import com.mybatisflex.spring.boot.MybatisFlexProperties;
import com.mybatisflex.spring.boot.SqlSessionFactoryBeanCustomizer;
import com.zznode.dhmp.mybatis.flex.mybatis.DhmpFlexConfiguration;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 处理SqlSessionFactoryBean，将{@link SqlSessionFactoryBean#configuration }设置为{@link DhmpFlexConfiguration}，以兼容pagehelper。
 * 不改变原有功能，只是将{@link FlexConfiguration}替换为{@link DhmpFlexConfiguration}
 *
 * @author 王俊
 */
public class DhmpSqlSessionFactoryBeanCustomizer implements SqlSessionFactoryBeanCustomizer {


    protected final List<ConfigurationCustomizer> configurationCustomizers;
    private final MybatisFlexProperties properties;

    public DhmpSqlSessionFactoryBeanCustomizer(MybatisFlexProperties mybatisFlexProperties, ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
        this.properties = mybatisFlexProperties;
        this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
    }

    @Override
    public void customize(SqlSessionFactoryBean factoryBean) {
        MybatisFlexProperties.CoreConfiguration coreConfiguration = this.properties.getConfiguration();
        final FlexConfiguration configuration;
        if (coreConfiguration != null || !StringUtils.hasText(this.properties.getConfigLocation())) {
            configuration = new DhmpFlexConfiguration();
        } else {
            configuration = null;
        }
        if (configuration != null && coreConfiguration != null) {
            // 由于CoreConfiguration的applyTo方法是默认权限，这里使用反射调用
            ReflectionUtils.doWithMethods(MybatisFlexProperties.CoreConfiguration.class, method -> {
                method.setAccessible(true);
                ReflectionUtils.invokeMethod(method, coreConfiguration, configuration);
            }, method -> "applyTo".equals(method.getName()));
        }
        if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
            for (ConfigurationCustomizer customizer : this.configurationCustomizers) {
                customizer.customize(configuration);
            }
        }
        factoryBean.setConfiguration(configuration);
    }
}
