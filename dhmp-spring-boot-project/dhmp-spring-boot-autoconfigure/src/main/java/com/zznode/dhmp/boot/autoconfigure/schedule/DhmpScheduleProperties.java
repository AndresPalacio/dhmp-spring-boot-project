package com.zznode.dhmp.boot.autoconfigure.schedule;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置属性
 *
 * @author 王俊
 */
@ConfigurationProperties(prefix = "dhmp.schedule")
public class DhmpScheduleProperties {

    private String scheduleServerUrl = "http://localhost:8100";

    public String getScheduleServerUrl() {
        return scheduleServerUrl;
    }

    public void setScheduleServerUrl(String scheduleServerUrl) {
        this.scheduleServerUrl = scheduleServerUrl;
    }
}
