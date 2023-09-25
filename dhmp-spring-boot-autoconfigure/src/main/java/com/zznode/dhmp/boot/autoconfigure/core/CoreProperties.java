package com.zznode.dhmp.boot.autoconfigure.core;

import com.zznode.dhmp.core.constant.Province;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 核心配置实体类
 *
 * @author 王俊
 * @date create in 2023/5/26 13:56
 */
@ConfigurationProperties("dhmp.core")
public class CoreProperties {

    private Province province;
    /**
     * 配置文件目录，请以路径分隔符结尾
     * <p>eg: "classpath:/"
     * <p>eg: "/usr/local/data/config/"
     */
    private String configLocation = "classpath:/";

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public String getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

}
