package com.zznode.dhmp.boot.autoconfigure.file;

import com.zznode.dhmp.boot.autoconfigure.web.internal.DhmpInternalRequestAutoConfiguration;
import com.zznode.dhmp.file.FileClient;
import com.zznode.dhmp.file.orm.FileInfoManager;
import com.zznode.dhmp.file.orm.RemoteFileInfoManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 文件客户端自动配置
 *
 * @author 王俊
 */
@AutoConfiguration(after = {DhmpInternalRequestAutoConfiguration.class})
@EnableConfigurationProperties(DhmpFileProperties.class)
@ConditionalOnClass({FileClient.class})
public class DhmpFileAutoConfiguration {
    private final DhmpFileProperties properties;

    public DhmpFileAutoConfiguration(DhmpFileProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "dhmp.file.remote-enabled", havingValue = "true", matchIfMissing = true)
    FileInfoManager fileInfoManager(RestTemplate restTemplate) {
        return new RemoteFileInfoManager(restTemplate, properties.getFileServiceUrl());
    }

    @Bean
    @ConditionalOnMissingBean
    FileClient fileClient(FileInfoManager fileInfoService) {
        return FileClient.builder()
                .basePath(this.properties.getBasePath())
                .fileInfoManager(fileInfoService)
                .build();
    }
}
