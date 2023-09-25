package com.zznode.dhmp.boot.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;

/**
 * 初始化，注入ProvinceCustomizerExcludeFilter
 *
 * @author 王俊
 * @date create in 2023/8/25
 *
 */
@Order(1)
public class ProvinceComponentContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final Logger logger = LoggerFactory.getLogger(ProvinceComponentContextInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        logger.debug("will exclude components annotated with @ProvinceComponent");
        // 注册 ProvinceCustomizerExcludeFilter
        applicationContext.getBeanFactory()
                .registerSingleton(ProvinceComponentExcludeFilter.class.getName(), new ProvinceComponentExcludeFilter());
    }
}
