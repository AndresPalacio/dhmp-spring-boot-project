package com.zznode.dhmp.boot.autoconfigure.context;

import com.zznode.dhmp.boot.autoconfigure.core.DhmpCoreAutoConfiguration;
import com.zznode.dhmp.context.ProvinceComponentScannerConfigurer;
import com.zznode.dhmp.context.util.ApplicationContextHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 省份自定义组件-自动配置
 *
 * @author 王俊
 * @date create in 2023/8/17
 */
@AutoConfiguration
@AutoConfigureBefore({DhmpCoreAutoConfiguration.class})
public class DhmpProvinceCustomizerAutoConfiguration {

    private static final Log logger = LogFactory.getLog(DhmpProvinceCustomizerAutoConfiguration.class);


    @Bean
    public ApplicationContextHelper applicationContextHelper(){
        return new ApplicationContextHelper();
    }


    @Configuration
    @Import(AutoConfiguredProvinceCustomizerBeanScannerRegistrar.class)
    @ConditionalOnMissingBean({ProvinceComponentScannerConfigurer.class})
    public static class ProvinceComponentScannerNotFoundConfiguration {

    }

    public static final class AutoConfiguredProvinceCustomizerBeanScannerRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware {

        private ListableBeanFactory beanFactory;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
            if (!AutoConfigurationPackages.has(this.beanFactory)) {
                logger.debug("Could not determine auto-configuration package, automatic mapper scanning disabled.");
                return;
            }
            List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
            if (logger.isDebugEnabled()) {
                packages.forEach(pkg -> logger.debug(String.format("Using auto-configuration base package '%s'", pkg)));
            }

            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ProvinceComponentScannerConfigurer.class);
            builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(packages));
            builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            registry.registerBeanDefinition(ProvinceComponentScannerConfigurer.class.getName(), builder.getBeanDefinition());
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = (ListableBeanFactory) beanFactory;
        }
    }
}
