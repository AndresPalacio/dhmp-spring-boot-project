package com.zznode.dhmp.boot.autoconfigure.security.oauth2.server;

import com.zznode.dhmp.core.message.DhmpMessageSource;
import com.zznode.dhmp.security.oauth2.server.authorization.DhmpOAuth2AuthorizationServerProperties;
import com.zznode.dhmp.security.oauth2.server.authorization.event.LoginSuccessListener;
import com.zznode.dhmp.security.oauth2.server.authorization.handler.AccountAuthenticationFailureHandler;
import com.zznode.dhmp.security.oauth2.server.authorization.handler.AccountStatusManageHandler;
import com.zznode.dhmp.security.oauth2.server.authorization.handler.DefaultAccountStatusManageHandler;
import com.zznode.dhmp.security.oauth2.server.authorization.handler.UserClearLogoutHandler;
import com.zznode.dhmp.security.oauth2.server.authorization.router.LoginRouterFunctionConfiguration;
import com.zznode.dhmp.security.oauth2.server.authorization.service.LoginService;
import com.zznode.dhmp.security.oauth2.server.authorization.service.UserAccountManager;
import com.zznode.dhmp.security.oauth2.server.authorization.service.impl.CustomUserDetailsManager;
import com.zznode.dhmp.security.oauth2.server.authorization.service.impl.DefaultUserAccountManager;
import com.zznode.dhmp.security.oauth2.server.authorization.service.impl.LoginServiceImpl;
import com.zznode.dhmp.security.oauth2.server.authorization.token.CustomUserDetailsOAuth2TokenCustomizer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * 组件配置
 *
 * @author 王俊
 * @date create in 2023/7/31 16:19
 */
@Configuration
public class AuthenticationComponentConfiguration implements InitializingBean {


    private final DhmpOAuth2AuthorizationServerProperties properties;

    public AuthenticationComponentConfiguration(DhmpOAuth2AuthorizationServerProperties properties) {
        this.properties = properties;
    }

    @Override
    public void afterPropertiesSet() {
        DhmpMessageSource.addBasename("messages.message_oauth");
    }


    @Bean
    @ConditionalOnMissingBean
    LoginService loginService() {
        return new LoginServiceImpl(properties);
    }

    @Bean
    public RouterFunction<ServerResponse> loginRouterFunction(LoginService loginService) {
        return LoginRouterFunctionConfiguration.loginRouter(loginService);
    }

    @Bean
    @ConditionalOnMissingBean
    public UserAccountManager userAccountManager(RestClient.Builder builder) {
        DefaultUserAccountManager defaultUserAccountManager = new DefaultUserAccountManager(builder.build());
        defaultUserAccountManager.setIamAddr(properties.getAccountManagement().getIamServerAddr());
        return defaultUserAccountManager;
    }


    @Bean
    @ConditionalOnMissingBean
    public UserDetailsService customUserService(UserAccountManager userAccountManager) {
        return new CustomUserDetailsManager(userAccountManager);
    }


    @Bean
    @ConditionalOnMissingBean(AccountStatusManageHandler.class)
    @ConditionalOnProperty(value = "dhmp.oauth2.authorizationserver.account-management.enabled", havingValue = "true")
    public AccountStatusManageHandler accountStatusHandler(UserAccountManager userAccountManager,
                                                           ObjectProvider<RedisTemplate<Object, Object>> redisTemplateProvider) {
        return new DefaultAccountStatusManageHandler(properties.getAccountManagement(),
                userAccountManager, redisTemplateProvider.getIfAvailable());
    }

    @Bean
    @ConditionalOnBean(AccountStatusManageHandler.class)
    public AccountAuthenticationFailureHandler customAuthenticationFailureHandler(
            AccountStatusManageHandler accountStatusManageHandler) {
        AccountAuthenticationFailureHandler handler =
                new AccountAuthenticationFailureHandler(accountStatusManageHandler);
        handler.setDefaultFailureUrl("/login?error");
        return handler;
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginSuccessListener loginSuccessListener(ObjectProvider<RedisTemplate<Object, Object>> redisTemplateProvider,
                                                     UserAccountManager userAccountManager) {
        RedisTemplate<Object, Object> redisTemplate = redisTemplateProvider.getIfAvailable();
        return new LoginSuccessListener(redisTemplate, userAccountManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public UserClearLogoutHandler tokenInvalidLogoutSuccessHandler(ObjectProvider<RedisTemplate<Object, Object>> redisTemplateProvider) {
        return new UserClearLogoutHandler(redisTemplateProvider.getIfAvailable());
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return new CustomUserDetailsOAuth2TokenCustomizer();
    }

}
