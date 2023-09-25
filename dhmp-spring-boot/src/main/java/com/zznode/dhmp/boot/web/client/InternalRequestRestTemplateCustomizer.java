package com.zznode.dhmp.boot.web.client;

import com.zznode.dhmp.core.constant.InternalRequestHeaders;
import com.zznode.dhmp.web.client.InternalTokenManager;
import com.zznode.dhmp.web.client.RequestHeaderCopier;
import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;

/**
 * RestTemplate 调用复制当前请求的token，同时携带内部调用的token
 *
 * @author 王俊
 * @date create in 2023/8/24
 */
public class InternalRequestRestTemplateCustomizer implements RestTemplateRequestCustomizer<ClientHttpRequest> {
    private final RequestHeaderCopier requestHeaderCopier;
    private final InternalTokenManager internalTokenManager;

    public InternalRequestRestTemplateCustomizer(RequestHeaderCopier requestHeaderCopier, InternalTokenManager internalTokenManager) {
        this.requestHeaderCopier = requestHeaderCopier;
        this.internalTokenManager = internalTokenManager;
    }

    @Override
    public void customize(ClientHttpRequest request) {
        HttpHeaders httpHeaders = request.getHeaders();
        requestHeaderCopier.copyHeaders(httpHeaders::set);
        httpHeaders.add(InternalRequestHeaders.INTERNAL_TOKEN, internalTokenManager.generateToken());
    }
}
