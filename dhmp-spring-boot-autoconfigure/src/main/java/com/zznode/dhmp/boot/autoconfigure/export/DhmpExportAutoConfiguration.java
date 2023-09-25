package com.zznode.dhmp.boot.autoconfigure.export;

import com.zznode.dhmp.boot.autoconfigure.lov.LovAutoConfiguration;
import com.zznode.dhmp.export.ExportAspect;
import com.zznode.dhmp.export.ExportConfig;
import com.zznode.dhmp.export.converter.LovConverter;
import com.zznode.dhmp.export.exporter.DefaultExporterFactory;
import com.zznode.dhmp.export.exporter.ExporterFactory;
import com.zznode.dhmp.export.utils.ExportHelper;
import com.zznode.dhmp.lov.client.LovClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@AutoConfiguration(after = LovAutoConfiguration.class)
@ConditionalOnClass({ExportAspect.class})
@Import({DhmpExportWebConfiguration.class})
public class DhmpExportAutoConfiguration  {

//    @Bean
//    ExportInitializingBean exportInitializingBean() {
//        return new ExportInitializingBean();
//    }

    @ConfigurationProperties("dhmp.export")
    @Bean
    @ConditionalOnMissingBean
    public ExportConfig exportConfig() {
        return new ExportConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public ExportHelper exportHelper(ExportConfig exportConfig,
                                     ObjectProvider<LovConverter> lovConverter) {
        ExportHelper exportHelper = new ExportHelper();
        exportHelper.setExportConfig(exportConfig);
        exportHelper.setLovConverter(lovConverter.getIfAvailable());
        return exportHelper;
    }

    @Bean
    @ConditionalOnMissingBean
    public ExportAspect exportAspect(ExportHelper exportHelper,
                                     ExporterFactory exporterFactory) {
        ExportAspect exportAspect = new ExportAspect();
        exportAspect.setExportFactory(exporterFactory);
        exportAspect.setExportHelper(exportHelper);
        return exportAspect;
    }

    @Bean
    @ConditionalOnMissingBean
    public ExporterFactory exporterFactory(ExportConfig exportConfig) {
        DefaultExporterFactory exporterFactory = new DefaultExporterFactory();
        exporterFactory.setExportConfig(exportConfig);
        return exporterFactory;
    }


    @ConditionalOnClass(LovClient.class)
    @ConditionalOnBean(LovClient.class)
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
