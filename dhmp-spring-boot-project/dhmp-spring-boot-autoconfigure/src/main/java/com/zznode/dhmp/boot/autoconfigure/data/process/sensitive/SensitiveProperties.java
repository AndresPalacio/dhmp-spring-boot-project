package com.zznode.dhmp.boot.autoconfigure.data.process.sensitive;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Set;

/**
 * 配置属性
 *
 * @author 王俊
 */
@ConfigurationProperties("dhmp.data.sensitive")
public class SensitiveProperties {
    /**
     * 默认处理字段与屏蔽模板映射
     */

    private Map<String, String> defaultProcessFieldPatternMap;

    private Set<String> defaultProcessFieldNames;

    public Set<String> getDefaultProcessFieldNames() {
        return defaultProcessFieldNames;
    }

    public void setDefaultProcessFieldNames(Set<String> defaultProcessFieldNames) {
        this.defaultProcessFieldNames = defaultProcessFieldNames;
    }

    public Map<String, String> getDefaultProcessFieldPatternMap() {
        return defaultProcessFieldPatternMap;
    }

    public void setDefaultProcessFieldPatternMap(Map<String, String> defaultProcessFieldPatternMap) {
        this.defaultProcessFieldPatternMap = defaultProcessFieldPatternMap;
    }
}
