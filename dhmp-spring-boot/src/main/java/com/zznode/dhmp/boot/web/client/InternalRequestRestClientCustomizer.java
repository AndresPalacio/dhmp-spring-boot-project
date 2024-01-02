package com.zznode.dhmp.boot.web.client;

import com.zznode.dhmp.core.constant.InternalRequestHeaders;
import com.zznode.dhmp.web.client.InternalTokenManager;
import com.zznode.dhmp.web.client.RequestHeaderCopier;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.web.client.RestClient;

/**
 * RestClient调用时，携带内部调用的token，以及当前请求的继承
 *
 * @author 王俊
 */
public class InternalRequestRestClientCustomizer implements RestClientCustomizer {

    private final InternalTokenManager internalTokenManager;

    public InternalRequestRestClientCustomizer(InternalTokenManager internalTokenManager) {
        this.internalTokenManager = internalTokenManager;
    }

    @Override
    public void customize(RestClient.Builder restClientBuilder) {

        restClientBuilder.defaultRequest(requestHeadersSpec -> requestHeadersSpec.headers(httpHeaders -> {
            RequestHeaderCopier.copyHeaders(httpHeaders::set);
            httpHeaders.add(InternalRequestHeaders.INTERNAL_TOKEN, internalTokenManager.generateToken());
        }));
    }
}
