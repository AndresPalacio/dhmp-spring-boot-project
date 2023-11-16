package com.zznode.dhmp.boot.autoconfigure.file;

import com.zznode.dhmp.file.DefaultFileClient;
import com.zznode.dhmp.file.FileClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 文件客户端自动配置
 *
 * @author 王俊
 */
@AutoConfiguration(after = {WebClientAutoConfiguration.class})
@EnableConfigurationProperties(DhmpFileProperties.class)
@ConditionalOnClass({FileClient.class})
public class DhmpFileAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    FileClient fileClient(WebClient.Builder builder, DhmpFileProperties properties) {
        return new DefaultFileClient(builder.build(), properties.getServer().getUri());
    }
}
