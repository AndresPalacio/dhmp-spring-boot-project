package com.zznode.dhmp.boot.autoconfigure.file;

import com.zznode.dhmp.data.web.RestResponseErrorHandler;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 描述
 *
 * @author 王俊
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
