package com.zznode.dhmp.boot.core;

import com.zznode.dhmp.core.constant.Province;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

/**
 * 程序初始化
 *
 * @author 王俊
 * @date create in 2023/5/12 10:00
 */
@Order(0)
public final class DhmpApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger logger = LoggerFactory.getLogger(DhmpApplicationContextInitializer.class);

    private static final String PROVINCE_PROPERTY_NAME = "dhmp.core.province";

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        // 注册省份信息
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Province province = environment.getRequiredProperty(PROVINCE_PROPERTY_NAME, Province.class);
        Province.setCurrentProvince(province);
        Province currentProvince = Province.currentProvince();
        String applicationName = getApplicationId(environment);
        logger.info("application {} running in province: {}",applicationName, currentProvince.getProvinceName());
    }

    private String getApplicationId(ConfigurableEnvironment environment) {
        String name = environment.getProperty("spring.application.name");
        return StringUtils.hasText(name) ? name : "application";
    }
}
