package com.zznode.dhmp.boot.autoconfigure.mybatis.plus;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.zznode.dhmp.core.exception.OptimisticLockException;
import com.zznode.dhmp.mybatis.interceptor.InsertAuditInnerInterceptor;
import com.zznode.dhmp.mybatis.interceptor.UpdateAuditInnerInterceptor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * MybatisPlusInterceptor配置
 *
 * @author 王俊
 * @date create in 2023/7/14 18:13
 */
@Configuration
@ConditionalOnClass({InsertAuditInnerInterceptor.class, UpdateAuditInnerInterceptor.class})
@ConditionalOnMissingBean(value = MybatisPlusInterceptor.class)
public class MybatisPlusInterceptorConfiguration {

    private static final List<InnerInterceptor> DEFAULT_INNER_INTERCEPTORS = new ArrayList<>();

    static {
        InsertAuditInnerInterceptor insertAuditInnerInterceptor = new InsertAuditInnerInterceptor();
        UpdateAuditInnerInterceptor updateAuditInnerInterceptor = new UpdateAuditInnerInterceptor();
        OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor = new OptimisticLockerInnerInterceptor();
        optimisticLockerInnerInterceptor.setException(new OptimisticLockException());
        DEFAULT_INNER_INTERCEPTORS.add(insertAuditInnerInterceptor);
        DEFAULT_INNER_INTERCEPTORS.add(updateAuditInnerInterceptor);
        DEFAULT_INNER_INTERCEPTORS.add(optimisticLockerInnerInterceptor);
    }

    public static List<InnerInterceptor> getDefaultInnerInterceptors() {
        return DEFAULT_INNER_INTERCEPTORS;
    }

    private final List<InnerInterceptor> innerInterceptors;

    public MybatisPlusInterceptorConfiguration(ObjectProvider<List<InnerInterceptor>> innerInterceptors) {
        this.innerInterceptors = innerInterceptors.getIfAvailable(() -> DEFAULT_INNER_INTERCEPTORS);
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        List<InnerInterceptor> innerInterceptors = CollectionUtil
                .addAllIfNotContains(DEFAULT_INNER_INTERCEPTORS, this.innerInterceptors);
        innerInterceptors.stream()
                .distinct()
                .forEach(interceptor::addInnerInterceptor);
        return interceptor;
    }


}
