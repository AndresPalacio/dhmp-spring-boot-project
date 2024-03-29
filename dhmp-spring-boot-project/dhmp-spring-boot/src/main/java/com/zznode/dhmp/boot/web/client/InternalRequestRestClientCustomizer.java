package com.zznode.dhmp.boot.web.client;

import com.zznode.dhmp.core.constant.InternalRequestHeaders;
import com.zznode.dhmp.web.client.InternalTokenManager;
import com.zznode.dhmp.web.client.RequestHeaderCopier;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.http.HttpHeaders;
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

        restClientBuilder
                .requestInitializer(request -> {
                    HttpHeaders httpHeaders = request.getHeaders();
                    RequestHeaderCopier.copyHeaders(httpHeaders::set);
                    httpHeaders.add(InternalRequestHeaders.INTERNAL_TOKEN, internalTokenManager.generateToken());
                });
//                此配置的header无法实时生成最新的token, 服务端获取到的token是过期的
//                .defaultHeaders(httpHeaders -> {
//                    RequestHeaderCopier.copyHeaders(httpHeaders::set);
//                    httpHeaders.add(InternalRequestHeaders.INTERNAL_TOKEN, internalTokenManager.generateToken());
//                })
//                TMD RestClient有毒,此配置无用
//                .defaultRequest(requestHeadersSpec -> requestHeadersSpec.headers(httpHeaders -> {
//                    RequestHeaderCopier.copyHeaders(httpHeaders::set);
//                    httpHeaders.add(InternalRequestHeaders.INTERNAL_TOKEN, internalTokenManager.generateToken());
//                }));
    }
}
