package com.zznode.dhmp.boot.autoconfigure.security.oauth2.server;

import com.zznode.dhmp.security.oauth2.server.authorization.DhmpOAuth2AuthorizationServerConfiguration;
import com.zznode.dhmp.security.oauth2.server.authorization.DhmpOAuth2AuthorizationServerProperties;
import com.zznode.dhmp.security.oauth2.server.authorization.configurer.DhmpFormLoginConfigurer;
import com.zznode.dhmp.security.oauth2.server.authorization.filter.ClearResourceFilter;
import com.zznode.dhmp.security.oauth2.server.authorization.handler.AccountAuthenticationFailureHandler;
import com.zznode.dhmp.security.oauth2.server.authorization.handler.UserClearLogoutHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Set;

/**
 * 授权服务器过滤器配置
 *
 * @author 王俊
 * @date create in 2023/6/13 17:35
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class DhmpAuthorizationServerConfiguration {

    private final DhmpOAuth2AuthorizationServerProperties properties;


    public DhmpAuthorizationServerConfiguration(DhmpOAuth2AuthorizationServerProperties properties) {
        this.properties = properties;
    }

    private static RequestMatcher createRequestMatcher() {
        MediaTypeRequestMatcher requestMatcher = new MediaTypeRequestMatcher(MediaType.TEXT_HTML);
        requestMatcher.setIgnoredMediaTypes(Set.of(MediaType.ALL));
        return requestMatcher;
    }

    /**
     * oauth2授权配置
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                               ObjectProvider<Customizer<HeadersConfigurer<HttpSecurity>>> headersConfigurerCustomizerObjectProvider) throws Exception {

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        DhmpOAuth2AuthorizationServerConfiguration.applyDefaultOauth2Security(http);
        http.exceptionHandling((exceptions) -> exceptions.defaultAuthenticationEntryPointFor(
                new LoginUrlAuthenticationEntryPoint(properties.getLoginUrl()), createRequestMatcher()));
        // 自定义响应头
        http.headers(headersConfigurerCustomizerObjectProvider.getIfAvailable(Customizer::withDefaults));
        return http.build();
    }

    /**
     * 登录配置
     */
    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                   ObjectProvider<AccountAuthenticationFailureHandler> accountAuthenticationFailureHandler,
                                                   UserClearLogoutHandler userClearLogoutHandler,
                                                   ObjectProvider<Customizer<HeadersConfigurer<HttpSecurity>>> headersConfigurerCustomizerObjectProvider) throws Exception {

        DhmpOAuth2AuthorizationServerConfiguration.applyDefaultSecurityLogin(http);

        http.logout(logout -> logout
                .addLogoutHandler(userClearLogoutHandler)
                .permitAll()
        );
        //noinspection unchecked
        DhmpFormLoginConfigurer<HttpSecurity> dhmpFormLoginConfigurer = http.getConfigurer(DhmpFormLoginConfigurer.class);
        dhmpFormLoginConfigurer.loginPage(properties.getLoginUrl())
                .passwordParameter(properties.getPasswordParameter())
                .usernameParameter(properties.getUsernameParameter())
                .passwordEncrypt(properties.getPasswordEncrypt())
                .usernameEncrypt(properties.getUsernameEncrypt())
                .defaultSuccessUrl(properties.getDefaultRedirectUrl())
                .permitAll();
        accountAuthenticationFailureHandler.ifAvailable(dhmpFormLoginConfigurer::failureHandler);

        // 自定义响应头，比如允许嵌入等
        http.headers(headersConfigurerCustomizerObjectProvider.getIfAvailable(Customizer::withDefaults));

        return http.build();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        DelegatingPasswordEncoder delegatingPasswordEncoder =
                (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());
        return delegatingPasswordEncoder;
    }

    @Bean
    public FilterRegistrationBean<ClearResourceFilter> registerClearResourceFilter() {
        FilterRegistrationBean<ClearResourceFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ClearResourceFilter());
        registration.addUrlPatterns("/*");
        registration.setName("clearResourceFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
