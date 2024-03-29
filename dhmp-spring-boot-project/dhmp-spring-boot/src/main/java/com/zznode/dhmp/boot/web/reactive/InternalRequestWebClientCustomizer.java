package com.zznode.dhmp.boot.web.reactive;

import com.zznode.dhmp.core.constant.InternalRequestHeaders;
import com.zznode.dhmp.web.client.InternalTokenManager;
import com.zznode.dhmp.web.client.RequestHeaderCopier;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient调用时，携带内部调用的token，以及当前请求的继承
 */
public class InternalRequestWebClientCustomizer implements WebClientCustomizer {

    private final InternalTokenManager internalTokenManager;

    public InternalRequestWebClientCustomizer(InternalTokenManager internalTokenManager) {
        this.internalTokenManager = internalTokenManager;
    }

    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        webClientBuilder.defaultRequest(requestHeadersSpec -> requestHeadersSpec.headers(httpHeaders -> {
            RequestHeaderCopier.copyHeaders(httpHeaders::set);
            httpHeaders.add(InternalRequestHeaders.INTERNAL_TOKEN, internalTokenManager.generateToken());
        }));
    }
}
