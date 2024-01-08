package com.zznode.dhmp.boot.autoconfigure.data.web;

import com.zznode.dhmp.data.web.RestResponseErrorHandler;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 给RestTemplate、RestClient设置自定义的响应错误处理
 * <p> 自定义异常返回的错误信息响应状态码是200,因此需要拦截
 *
 * @author 王俊
 */
public class ExceptionRestCustomizer implements RestTemplateCustomizer, RestClientCustomizer {
    @Override
    public void customize(RestTemplate restTemplate) {
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        restTemplate.setErrorHandler(responseErrorHandler(messageConverters));
    }

    @Override
    public void customize(RestClient.Builder restClientBuilder) {

        restClientBuilder.messageConverters(messageConverters -> {
            restClientBuilder.defaultStatusHandler(responseErrorHandler(messageConverters));
        });
    }

    private ResponseErrorHandler responseErrorHandler(List<HttpMessageConverter<?>> messageConverters) {
        RestResponseErrorHandler responseErrorHandler = new RestResponseErrorHandler();
        responseErrorHandler.setMessageConverters(messageConverters);
        return responseErrorHandler;
    }
}
