package com.zznode.dhmp.boot.autoconfigure.web.internal;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * 内部调用配置
 *
 * @author 王俊
 * @date create in 2023/8/25
 */
@ConfigurationProperties("dhmp.web.internal")
public class DhmpWebInternalProperties {
    /**
     * 内部token内容，可以随便填,但是每个服务必须配置一致,最好使用配置中心
     */
    private String internalToken = "DHMP";
    /**
     * token过期时间
     *
     */
    private Duration tokenAliveTime = Duration.ofSeconds(30);

    public String getInternalToken() {
        return internalToken;
    }

    public void setInternalToken(String internalToken) {
        this.internalToken = internalToken;
    }

    public Duration getTokenAliveTime() {
        return tokenAliveTime;
    }

    public void setTokenAliveTime(Duration tokenAliveTime) {
        this.tokenAliveTime = tokenAliveTime;
    }
}
