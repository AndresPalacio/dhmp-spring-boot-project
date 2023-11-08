package com.zznode.dhmp.boot.autoconfigure.file;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 描述
 *
 * @author 王俊
 */
@ConfigurationProperties("dhmp.file")
public class DhmpFileProperties {

    private Client client;
    private Manage manage;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Manage getManage() {
        return manage;
    }

    public void setManage(Manage manage) {
        this.manage = manage;
    }

    public static class Client {

        private Boolean remote;

        private String basePath;

        public Boolean getRemote() {
            return remote;
        }

        public void setRemote(Boolean remote) {
            this.remote = remote;
        }

        public String getBasePath() {
            return basePath;
        }

        public void setBasePath(String basePath) {
            this.basePath = basePath;
        }
    }

    public static class Manage {

        private Boolean remote;
        private String fileServiceUrl;

        public Boolean getRemote() {
            return remote;
        }

        public void setRemote(Boolean remote) {
            this.remote = remote;
        }

        public String getFileServiceUrl() {
            return fileServiceUrl;
        }

        public void setFileServiceUrl(String fileServiceUrl) {
            this.fileServiceUrl = fileServiceUrl;
        }
    }
}
