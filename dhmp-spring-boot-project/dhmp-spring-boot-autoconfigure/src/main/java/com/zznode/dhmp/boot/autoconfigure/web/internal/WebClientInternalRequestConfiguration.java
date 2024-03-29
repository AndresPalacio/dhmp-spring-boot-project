package com.zznode.dhmp.boot.autoconfigure.web.internal;


import com.zznode.dhmp.boot.web.reactive.InternalRequestWebClientCustomizer;
import com.zznode.dhmp.web.client.InternalTokenManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConditionalOnClass(WebClient.class)
public class WebClientInternalRequestConfiguration {

    @Bean
    InternalRequestWebClientCustomizer internalRequestWebClientCustomizer(InternalTokenManager internalTokenManager){
        return new InternalRequestWebClientCustomizer(internalTokenManager);
    }
}
