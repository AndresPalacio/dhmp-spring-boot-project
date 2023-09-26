package com.zznode.dhmp.boot.autoconfigure.security.oauth2.resource;

import com.zznode.dhmp.security.oauth2.server.resource.authentication.DhmpJwtAuthenticationConverter;
import com.zznode.dhmp.security.oauth2.server.resource.authentication.PermissionAuthorizationManager;
import com.zznode.dhmp.web.client.InternalTokenManager;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 描述
 *
 * @author 王俊
 * @date create in 2023/5/19 10:54
 */
@Configuration
public class DhmpResourceServerConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(RequestMappingHandlerMapping.class)
    static class MvcPermissionCheckConfiguration {

        @Bean
        public PermissionAuthorizationManager permissionAuthorizationManager(ObjectProvider<RequestMappingHandlerMapping> handlerMapping,
                                                                             ObjectProvider<InternalTokenManager> internalTokenManager) {
            return new PermissionAuthorizationManager(handlerMapping.getIfAvailable(), internalTokenManager.getIfAvailable());
        }

    }

    @Configuration(proxyBeanMethods = false)
    @EnableWebSecurity
    static class OAuth2SecurityFilterChainConfiguration {
        @Bean
        @Order(SecurityProperties.DEFAULT_FILTER_ORDER)
        SecurityFilterChain resourceSecurityFilterChain(
                HttpSecurity http,
                AuthenticationEntryPoint bearerTokenAuthenticationEntryPoint,
                ObjectProvider<PermissionAuthorizationManager> permissionAuthorizationManagerObjectProvider
        ) throws Exception {
            http.authorizeHttpRequests(configurer -> {
                        PermissionAuthorizationManager permissionAuthorizationManager = permissionAuthorizationManagerObjectProvider.getIfUnique();
                        if (permissionAuthorizationManager != null) {
                            configurer
                                    .anyRequest()
                                    .access(permissionAuthorizationManager);
                        } else {
                            configurer.anyRequest().authenticated();
                        }
                    })
                    .cors(Customizer.withDefaults())
                    .exceptionHandling(exceptions -> {
                        // 错误返回403
                    })
                    .oauth2ResourceServer(configurer ->
                            configurer.jwt(jwtConfigurer ->
                                            jwtConfigurer.jwtAuthenticationConverter(new DhmpJwtAuthenticationConverter())
                                    )
                                    .authenticationEntryPoint(bearerTokenAuthenticationEntryPoint)

                    );
            return http.build();
        }

        /**
         * token认证失败处理,注册为bean,让子类可以进行覆盖
         *
         * @return BearerTokenAuthenticationEntryPoint
         */
        @Bean
        @ConditionalOnMissingBean
        public AuthenticationEntryPoint bearerTokenAuthenticationEntryPoint() {

            return new BearerTokenAuthenticationEntryPoint();
        }
    }


}
