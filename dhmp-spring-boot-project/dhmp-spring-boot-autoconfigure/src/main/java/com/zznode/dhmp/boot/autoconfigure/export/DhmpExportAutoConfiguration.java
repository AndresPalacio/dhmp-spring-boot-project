package com.zznode.dhmp.boot.autoconfigure.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zznode.dhmp.boot.autoconfigure.lov.LovAutoConfiguration;
import com.zznode.dhmp.export.ExportContext;
import com.zznode.dhmp.export.config.ExportConfigProperties;
import com.zznode.dhmp.export.config.ExportConfiguration;
import com.zznode.dhmp.export.config.ExportConfigurer;
import com.zznode.dhmp.export.converter.LovConverter;
import com.zznode.dhmp.export.support.exporter.DefaultExporterFactory;
import com.zznode.dhmp.export.support.exporter.ExporterFactory;
import com.zznode.dhmp.export.utils.ExportHelper;
import com.zznode.dhmp.lov.client.LovClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 导出-自动配置类
 *
 * @author 王俊
 * @date create in 2023/8/30
 */
@AutoConfiguration(after = {LovAutoConfiguration.class, JacksonAutoConfiguration.class})
@ConditionalOnClass({ExportContext.class})
@Import({ExportConfiguration.class})
public class DhmpExportAutoConfiguration {


    @Bean
    @ConfigurationProperties("dhmp.export")
    public ExportConfigProperties exportConfigProperties() {
        return new ExportConfigProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public ExportHelper exportHelper(ObjectProvider<LovConverter> lovConverters, ObjectProvider<ObjectMapper> objectMappers) {
        ExportHelper exportHelper = new ExportHelper();
        lovConverters.ifAvailable(exportHelper::setLovConverter);
        objectMappers.ifAvailable(exportHelper::setObjectMapper);
        return exportHelper;
    }


    @Bean
    @ConditionalOnMissingBean
    public ExporterFactory exporterFactory(ExportConfigProperties exportConfig, ExportHelper exportHelper) {
        DefaultExporterFactory exporterFactory = new DefaultExporterFactory();
        exporterFactory.setExportConfig(exportConfig);
        exporterFactory.setExportHelper(exportHelper);
        return exporterFactory;
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(ExportConfigurer.class)
    static class ExporterFactoryConfiguration implements ExportConfigurer {

        private final ObjectProvider<ExporterFactory> exporterFactory;

        ExporterFactoryConfiguration(ObjectProvider<ExporterFactory> exporterFactory) {
            this.exporterFactory = exporterFactory;
        }

        @Override
        public ExporterFactory getExporterFactory() {
            return exporterFactory.getIfAvailable();
        }

    }


    @ConditionalOnClass(LovClient.class)
    @ConditionalOnBean(LovClient.class)
    @ConditionalOnMissingBean(LovConverter.class)
    @Configuration
    static class LovConverterRegisterConfiguration {

        @Bean
        @ConditionalOnBean(LovClient.class)
        public LovConverter lovConverter(LovClient lovClient) {
            LovConverter lovConverter = new LovConverter();
            lovConverter.setLovClient(lovClient);
            return lovConverter;
        }

    }
}
