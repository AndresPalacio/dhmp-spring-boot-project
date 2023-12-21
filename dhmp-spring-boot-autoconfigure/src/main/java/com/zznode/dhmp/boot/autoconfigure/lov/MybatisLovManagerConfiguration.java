package com.zznode.dhmp.boot.autoconfigure.lov;

import com.zznode.dhmp.boot.autoconfigure.mybatis.flex.DhmpMybatisFlexAutoConfiguration;
import com.zznode.dhmp.boot.autoconfigure.mybatis.plus.DhmpMybatisPlusAutoConfiguration;
import com.zznode.dhmp.lov.LovManager;
import com.zznode.dhmp.lov.support.manager.MybatisLovManager;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-lov-manager配置
 *
 * @author 王俊
 * @date create in 2023/9/1
 */
@Configuration
@ConditionalOnClass({SqlSessionFactory.class, MybatisLovManager.class})
@AutoConfigureAfter({DhmpMybatisPlusAutoConfiguration.class, DhmpMybatisFlexAutoConfiguration.class})
public class MybatisLovManagerConfiguration {

    @Bean
    @ConditionalOnMissingBean
    LovManager mybatisLovManager(SqlSessionFactory sqlSessionFactory) {
        return new MybatisLovManager(sqlSessionFactory);
    }
}
