package com.zznode.dhmp.boot.autoconfigure.data.exception;

import com.zznode.dhmp.data.exception.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * 描述
 *
 * @author 王俊
 * @date create in 2023/8/29
 */
@AutoConfiguration
@ConditionalOnClass(GlobalExceptionHandler.class)
public class DhmpExceptionAutoConfiguration {

    @Bean
    public GlobalExceptionHandler globalExceptionHandler(){
        return new GlobalExceptionHandler();
    }
}
