package com.zznode.dhmp.boot.autoconfigure.web.internal;

import com.zznode.dhmp.boot.web.client.InternalRequestRestTemplateCustomizer;
import com.zznode.dhmp.web.client.InternalTokenManager;
import com.zznode.dhmp.web.client.RequestHeaderCopier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;

/**
 * 内部调用相关自动配置
 *
 * @author 王俊
 * @date create in 2023/8/25
 */
@AutoConfiguration(before = {RestTemplateAutoConfiguration.class})
@EnableConfigurationProperties(DhmpWebInternalProperties.class)
@ConditionalOnClass({InternalTokenManager.class})
@Import({RestTemplateInternalRequestConfiguration.class, WebClientInternalRequestConfiguration.class})
public class DhmpInternalRequestAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DefaultInternalTokenManager defaultInternalTokenManager(DhmpWebInternalProperties properties) {

        return new DefaultInternalTokenManager(properties);
    }

    @Bean
    public RequestHeaderCopier requestHeaderCopier() {
        return new RequestHeaderCopier();
    }


}
