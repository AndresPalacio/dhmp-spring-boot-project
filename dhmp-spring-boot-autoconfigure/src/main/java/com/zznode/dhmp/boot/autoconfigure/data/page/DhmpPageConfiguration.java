package com.zznode.dhmp.boot.autoconfigure.data.page;

import com.zznode.dhmp.data.page.parser.DefaultOrderByParser;
import com.zznode.dhmp.data.page.parser.OrderByParser;
import com.zznode.dhmp.data.page.web.DhmpPageableHandlerMethodArgumentResolver;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.util.Lazy;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.data.web.config.SpringDataWebConfiguration;

/**
 * 分页web配置
 *
 * @author 王俊
 * @date create in 2023/7/3 15:20
 */
@Configuration
public class DhmpPageConfiguration extends SpringDataWebConfiguration {

    private final Lazy<SortHandlerMethodArgumentResolver> sortResolver;

    public DhmpPageConfiguration(ApplicationContext context,
                                 @Qualifier("mvcConversionService") ObjectFactory<ConversionService> conversionService) {
        super(context, conversionService);
        this.sortResolver = Lazy
                .of(() -> context.getBean("sortResolver", SortHandlerMethodArgumentResolver.class));
    }

    @Bean
    @ConditionalOnMissingBean
    public OrderByParser orderByParser() {
        return new DefaultOrderByParser();
    }

    @Override
    @Bean
    public PageableHandlerMethodArgumentResolver pageableResolver() {
        DhmpPageableHandlerMethodArgumentResolver pageableResolver = new DhmpPageableHandlerMethodArgumentResolver(
                this.sortResolver.get());
        customizePageableResolver(pageableResolver);
        return pageableResolver;
    }

}
