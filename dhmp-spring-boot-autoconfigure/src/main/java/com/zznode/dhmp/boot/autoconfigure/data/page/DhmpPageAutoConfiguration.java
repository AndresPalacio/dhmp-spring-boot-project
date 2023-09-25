package com.zznode.dhmp.boot.autoconfigure.data.page;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.data.web.config.SortHandlerMethodArgumentResolverCustomizer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 描述
 *
 * @author 王俊
 * @date create in 2023/7/3 15:09
 */
@AutoConfiguration
@AutoConfigureBefore(SpringDataWebAutoConfiguration.class)
@ConditionalOnClass({WebMvcConfigurer.class, com.zznode.dhmp.data.page.domain.PageRequest.class})
@Import({DhmpPageConfiguration.class})
@EnableConfigurationProperties(SpringDataWebProperties.class)
public class DhmpPageAutoConfiguration {

    private final SpringDataWebProperties properties;

    public DhmpPageAutoConfiguration(SpringDataWebProperties properties) {
        this.properties = properties;
    }

    /**
     * 请不要设置{@link SpringDataWebProperties.Pageable#setOneIndexedParameters(boolean)}, 保持为false，否则会出问题
     *
     * @return 自定义page参数解析器
     */
    @Bean
    @ConditionalOnMissingBean
    public PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer() {

        return (resolver) -> {
            SpringDataWebProperties.Pageable pageable = this.properties.getPageable();
            resolver.setPageParameterName(pageable.getPageParameter());
            resolver.setSizeParameterName(pageable.getSizeParameter());
            resolver.setPrefix(pageable.getPrefix());
            resolver.setQualifierDelimiter(pageable.getQualifierDelimiter());
            // 使用1基索引分页
            resolver.setOneIndexedParameters(false);
            resolver.setFallbackPageable(PageRequest.of(1, pageable.getDefaultPageSize()));
            resolver.setMaxPageSize(pageable.getMaxPageSize());

        };
    }

    @Bean
    @ConditionalOnMissingBean
    public SortHandlerMethodArgumentResolverCustomizer sortCustomizer() {
        return (resolver) -> resolver.setSortParameter(this.properties.getSort().getSortParameter());
    }
}
