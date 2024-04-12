package com.zznode.dhmp.boot.core;

import com.zznode.dhmp.core.constant.Province;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private static final Log logger = LogFactory.getLog(DhmpApplicationContextInitializer.class);

    private static final String PROVINCE_PROPERTY_NAME = "dhmp.core.province";

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        // 注册省份信息
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Province province = environment.getRequiredProperty(PROVINCE_PROPERTY_NAME, Province.class);
        Province.setCurrentProvince(province);
        Province currentProvince = Province.currentProvince();
        String applicationName = getApplicationId(environment);
        logger.info(String.format("application %s running in province: %s", applicationName, currentProvince.getProvinceName()));
    }

    private String getApplicationId(ConfigurableEnvironment environment) {
        String name = environment.getProperty("spring.application.name");
        return StringUtils.hasText(name) ? name : "application";
    }
}
