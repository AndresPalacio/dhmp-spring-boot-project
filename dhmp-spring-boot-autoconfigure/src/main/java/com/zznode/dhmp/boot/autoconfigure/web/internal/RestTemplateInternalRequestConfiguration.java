package com.zznode.dhmp.boot.autoconfigure.web.internal;

import com.zznode.dhmp.boot.web.client.InternalRequestRestTemplateCustomizer;
import com.zznode.dhmp.web.client.InternalTokenManager;
import com.zznode.dhmp.web.client.RequestHeaderCopier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConditionalOnClass(RestTemplate.class)
public class RestTemplateInternalRequestConfiguration {
    @Bean
    public InternalRequestRestTemplateCustomizer internalRequestRestTemplateCustomizer(RequestHeaderCopier requestHeaderCopier,
                                                                                       InternalTokenManager internalTokenManager) {
        return new InternalRequestRestTemplateCustomizer(requestHeaderCopier, internalTokenManager);
    }

    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }
}
