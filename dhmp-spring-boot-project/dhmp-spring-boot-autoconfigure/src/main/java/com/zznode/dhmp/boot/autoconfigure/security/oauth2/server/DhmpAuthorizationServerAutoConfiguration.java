package com.zznode.dhmp.boot.autoconfigure.security.oauth2.server;

import com.zznode.dhmp.security.oauth2.server.authorization.DhmpOAuth2AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

/**
 * 授权服务器自动配置
 *
 * @author 王俊
 * @date create in 2023/6/13 14:31
 */
@AutoConfiguration(before = {OAuth2AuthorizationServerAutoConfiguration.class})
@Import({DhmpAuthorizationServerConfiguration.class, AuthenticationComponentConfiguration.class})
@ConditionalOnClass({DhmpOAuth2AuthorizationServerProperties.class, OAuth2Authorization.class})
public class DhmpAuthorizationServerAutoConfiguration {

    @Bean
    @ConfigurationProperties("dhmp.oauth2.authorizationserver")
    public DhmpOAuth2AuthorizationServerProperties dhmpOAuth2AuthorizationServerProperties() {
        return new DhmpOAuth2AuthorizationServerProperties();
    }
}
