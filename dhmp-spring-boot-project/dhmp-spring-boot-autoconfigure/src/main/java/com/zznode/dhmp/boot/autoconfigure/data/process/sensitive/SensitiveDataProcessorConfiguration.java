package com.zznode.dhmp.boot.autoconfigure.data.process.sensitive;

import com.zznode.dhmp.data.process.sensitive.SensitiveDataProcessor;
import com.zznode.dhmp.data.process.sensitive.config.SensitiveConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 敏感数据处理器配置
 *
 * @author 王俊
 */
@Configuration(proxyBeanMethods = false)
public class SensitiveDataProcessorConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public SensitiveDataProcessor sensitiveDataProcessor(SensitiveProperties sensitiveProperties) {
        SensitiveDataProcessor processor = new SensitiveDataProcessor();
        processor.setDefaultProcessFieldNames(sensitiveProperties.getDefaultProcessFieldNames());
        return processor;
    }

    @Bean
    @ConditionalOnMissingBean
    SensitiveConfigurer sensitiveDataProcessorConfigurer(SensitiveDataProcessor sensitiveDataProcessor) {
        return new SensitiveDataProcessorConfigurer(sensitiveDataProcessor);
    }

    static final class SensitiveDataProcessorConfigurer implements SensitiveConfigurer {
        private final SensitiveDataProcessor processor;

        SensitiveDataProcessorConfigurer(SensitiveDataProcessor sensitiveDataProcessor) {
            this.processor = sensitiveDataProcessor;
        }

        @Override
        public SensitiveDataProcessor sensitiveDataProcessor() {
            return processor;
        }
    }

}
