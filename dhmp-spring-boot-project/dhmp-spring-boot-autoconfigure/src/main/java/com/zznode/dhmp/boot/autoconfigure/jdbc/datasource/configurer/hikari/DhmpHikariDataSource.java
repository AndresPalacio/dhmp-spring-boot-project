package com.zznode.dhmp.boot.autoconfigure.jdbc.datasource.configurer.hikari;

import com.zaxxer.hikari.HikariDataSource;
import com.zznode.dhmp.boot.jdbc.datasource.DataSourceAware;
import com.zznode.dhmp.core.utils.AesUtil;

/**
 * 自定义Hikari数据源，继承自{@link HikariDataSource}
 * <p>重写获取密码方法，如果{@link #enablePasswordEncrypt}为true，用于解密配置文件中的密码
 *
 * @author 王俊
 * @date create in 2023/5/25 17:33
 */
public class DhmpHikariDataSource extends HikariDataSource implements DataSourceAware {

    private boolean enablePasswordEncrypt = false;

    @Override
    public void setEnablePasswordEncrypt(boolean enablePasswordEncrypt) {
        this.enablePasswordEncrypt = enablePasswordEncrypt;
    }

    public String getOriginPassword() {
        return super.getPassword();
    }

    @Override
    public String getPassword() {
        String password = super.getPassword();
        if (this.enablePasswordEncrypt) {
            return AesUtil.decryptData(password);
        }
        return password;
    }
}
