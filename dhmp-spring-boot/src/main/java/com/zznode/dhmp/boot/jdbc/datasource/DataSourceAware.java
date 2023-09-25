package com.zznode.dhmp.boot.jdbc.datasource;

import org.springframework.beans.factory.Aware;

/**
 * 自定义数据源超接口，用于进行特殊配置
 *
 * @author 王俊
 * @date create in 2023/5/26 18:06
 */
public interface DataSourceAware extends Aware {

    /**
     * 设置是否密码加密
     * @param enablePasswordEncrypt 是否开启密码加密
     */
    void setEnablePasswordEncrypt(boolean enablePasswordEncrypt);

}
