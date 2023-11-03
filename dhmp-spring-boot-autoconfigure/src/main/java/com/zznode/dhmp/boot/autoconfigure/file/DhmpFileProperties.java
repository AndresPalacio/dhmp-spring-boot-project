package com.zznode.dhmp.boot.autoconfigure.file;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 描述
 *
 * @author 王俊
 */
@ConfigurationProperties("dhmp.file")
public class DhmpFileProperties {

    private String basePath;
    private Boolean remoteEnabled;
    private String fileServiceUrl;

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public Boolean getRemoteEnabled() {
        return remoteEnabled;
    }

    public void setRemoteEnabled(Boolean remoteEnabled) {
        this.remoteEnabled = remoteEnabled;
    }

    public String getFileServiceUrl() {
        return fileServiceUrl;
    }

    public void setFileServiceUrl(String fileServiceUrl) {
        this.fileServiceUrl = fileServiceUrl;
    }
}
