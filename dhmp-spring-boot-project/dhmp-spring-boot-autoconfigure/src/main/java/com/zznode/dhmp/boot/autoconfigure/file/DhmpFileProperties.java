package com.zznode.dhmp.boot.autoconfigure.file;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件配置属性
 *
 * @author 王俊
 */
@ConfigurationProperties("dhmp.file")
public class DhmpFileProperties {

    private Server server;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public static class Server {
        private String uri;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

}
