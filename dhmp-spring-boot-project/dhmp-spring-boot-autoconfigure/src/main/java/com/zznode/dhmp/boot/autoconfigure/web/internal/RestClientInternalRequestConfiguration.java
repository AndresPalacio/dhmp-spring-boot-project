package com.zznode.dhmp.boot.autoconfigure.web.internal;

import com.zznode.dhmp.boot.web.client.InternalRequestRestClientCustomizer;
import com.zznode.dhmp.web.client.InternalTokenManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;


@Configuration
@ConditionalOnClass(RestClient.class)
public class RestClientInternalRequestConfiguration {
    @Bean
    public InternalRequestRestClientCustomizer internalRequestRestClientCustomizer(InternalTokenManager internalTokenManager) {
        return new InternalRequestRestClientCustomizer(internalTokenManager);
    }
}
