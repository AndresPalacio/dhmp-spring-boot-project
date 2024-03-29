package com.zznode.dhmp.boot.autoconfigure.mybatis.flex.customizer;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.activerecord.Model;
import com.zznode.dhmp.mybatis.flex.config.customizer.DhmpMybatisFlexCustomizer;
import com.zznode.dhmp.mybatis.flex.domain.BaseModel;
import com.zznode.dhmp.mybatis.flex.listener.insert.InsertAuditListener;
import com.zznode.dhmp.mybatis.flex.listener.set.DelegatingSetListener;
import com.zznode.dhmp.mybatis.flex.listener.update.UpdateAuditListener;
import org.springframework.core.Ordered;

/**
 * 默认配置
 *
 * @author 王俊
 */
public class DefaultDhmpMybatisFlexCustomizer implements DhmpMybatisFlexCustomizer {

    private final DelegatingSetListener setListener;

    public DefaultDhmpMybatisFlexCustomizer(DelegatingSetListener setListener) {
        this.setListener = setListener;
    }

    /**
     * 自定义 MyBatis-Flex 配置。
     *
     * @param globalConfig 全局配置
     */
    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        // 修改将setListener注册到Model上,因为大多数场景的实体类都没必要继承BaseModel
        globalConfig.registerSetListener(setListener, Model.class);
        globalConfig.registerInsertListener(new InsertAuditListener(), BaseModel.class);
        globalConfig.registerUpdateListener(new UpdateAuditListener(), BaseModel.class);
    }

    @Override
    public int getOrder() {
        // 优先级最高，最先调用，会被其他的覆盖
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
