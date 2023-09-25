package com.zznode.dhmp.boot.autoconfigure.core;

import com.zznode.dhmp.core.message.DhmpMessageSource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;

/**
 * 描述
 *
 * @author 王俊
 * @date create in 2023/6/28 14:05
 */
@AutoConfiguration
@ConditionalOnClass(DhmpMessageSource.class)
public class MessageSourceAutoConfiguration {
    @Bean
    public MessageSource messageSource() {
        return DhmpMessageSource.getInstance();
    }
}
