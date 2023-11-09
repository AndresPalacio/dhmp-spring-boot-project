package com.zznode.dhmp.boot.web.reactive;

import com.zznode.dhmp.core.constant.InternalRequestHeaders;
import com.zznode.dhmp.web.client.InternalTokenManager;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient调用时，携带内部调用的token
 */
public class InternalRequestWebClientCustomizer implements WebClientCustomizer {

    private final InternalTokenManager internalTokenManager;

    public InternalRequestWebClientCustomizer(InternalTokenManager internalTokenManager) {
        this.internalTokenManager = internalTokenManager;
    }

    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        webClientBuilder.defaultHeaders(httpHeaders ->
                httpHeaders.add(InternalRequestHeaders.INTERNAL_TOKEN, internalTokenManager.generateToken()));
    }
}
