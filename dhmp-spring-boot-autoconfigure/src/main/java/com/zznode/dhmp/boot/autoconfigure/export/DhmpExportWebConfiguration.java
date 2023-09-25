package com.zznode.dhmp.boot.autoconfigure.export;

import com.zznode.dhmp.export.utils.ExportHelper;
import com.zznode.dhmp.export.web.ExportContextHandlerMethodArgumentResolver;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Lazy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 导出web配置
 *
 * @author 王俊
 * @date create in 2023/9/6
 */
@Configuration
@ConditionalOnClass({WebMvcConfigurer.class})
public class DhmpExportWebConfiguration implements WebMvcConfigurer {

    private final Lazy<ExportContextHandlerMethodArgumentResolver> exportContextResolver;


    public DhmpExportWebConfiguration(ApplicationContext context) {
        this.exportContextResolver = Lazy.of(() -> context.getBean(ExportContextHandlerMethodArgumentResolver.class));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(exportContextResolver.get());
    }

    @Bean
    ExportContextHandlerMethodArgumentResolver exportContextHandlerMethodArgumentResolver(ObjectProvider<ExportHelper> exportHelper) {
        return new ExportContextHandlerMethodArgumentResolver(exportHelper);
    }
}
