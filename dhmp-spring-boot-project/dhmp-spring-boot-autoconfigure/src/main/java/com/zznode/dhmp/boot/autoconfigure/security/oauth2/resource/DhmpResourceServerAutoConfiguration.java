package com.zznode.dhmp.boot.autoconfigure.security.oauth2.resource;

import com.zznode.dhmp.security.oauth2.server.resource.authentication.DhmpJwtAuthenticationConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;

/**
 * 资源服务-自动配置
 *
 * @author 王俊
 */
@AutoConfiguration(before = OAuth2ResourceServerAutoConfiguration.class)
@ConditionalOnClass({BearerTokenAuthenticationToken.class, DhmpJwtAuthenticationConverter.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import({
        DhmpResourceServerConfiguration.OAuth2SecurityFilterChainConfiguration.class,
        DhmpResourceServerConfiguration.MvcPermissionCheckConfiguration.class
})
public class DhmpResourceServerAutoConfiguration {

}
