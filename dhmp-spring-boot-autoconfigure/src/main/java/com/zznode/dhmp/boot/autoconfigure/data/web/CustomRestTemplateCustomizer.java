package com.zznode.dhmp.boot.autoconfigure.data.web;

import com.zznode.dhmp.data.web.RestResponseErrorHandler;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 给RestTemplate设置自定义的响应错误处理
 * <p> 自定义异常返回的错误信息响应状态码是200,因此需要拦截
 *
 * @author 王俊
 * @see RestResponseErrorHandler
 */
public class CustomRestTemplateCustomizer implements RestTemplateCustomizer {
    @Override
    public void customize(RestTemplate restTemplate) {

        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        RestResponseErrorHandler restResponseErrorHandler = new RestResponseErrorHandler();
        restResponseErrorHandler.setMessageConverters(messageConverters);
        restTemplate.setErrorHandler(restResponseErrorHandler);
    }
}
