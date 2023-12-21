package com.zznode.dhmp.boot.autoconfigure.mybatis.flex;

import com.mybatisflex.annotation.SetListener;
import com.mybatisflex.spring.FlexSqlSessionFactoryBean;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import com.mybatisflex.spring.boot.MybatisFlexAutoConfiguration;
import com.zznode.dhmp.boot.autoconfigure.mybatis.DhmpMybatisConfiguration;
import com.zznode.dhmp.boot.autoconfigure.mybatis.flex.customizer.DefaultDhmpMybatisFlexCustomizer;
import com.zznode.dhmp.boot.autoconfigure.mybatis.flex.customizer.DhmpMybatisFlexCustomizerComposite;
import com.zznode.dhmp.lov.client.LovClient;
import com.zznode.dhmp.mybatis.flex.config.DelegatingDhmpFlexConfiguration;
import com.zznode.dhmp.mybatis.flex.config.DhmpFlexConfigurer;
import com.zznode.dhmp.mybatis.flex.config.customizer.DhmpMybatisFlexCustomizer;
import com.zznode.dhmp.mybatis.flex.listener.set.DelegatingSetListener;
import com.zznode.dhmp.mybatis.flex.listener.set.LovSetListener;
import com.zznode.dhmp.mybatis.flex.listener.set.SupportedListener;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

/**
 * Mybatis-Flex自动配置
 *
 * @author 王俊
 */

@AutoConfiguration
@AutoConfigureBefore({MybatisFlexAutoConfiguration.class})
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class, FlexSqlSessionFactoryBean.class})
@Import({DhmpMybatisConfiguration.class})
public class DhmpMybatisFlexAutoConfiguration {


    @Configuration(proxyBeanMethods = false)
    @Import({EnableDhmpMybatisFlexConfiguration.class, LovSetListenerConfiguration.class})
    public static class DhmpMybatisFlexAutoConfigurationAdepter implements DhmpFlexConfigurer {

        private final ObjectProvider<DhmpMybatisFlexCustomizer> customizerObjectProvider;
        private final ObjectProvider<SupportedListener> listenerObjectProvider;

        public DhmpMybatisFlexAutoConfigurationAdepter(
                ObjectProvider<DhmpMybatisFlexCustomizer> customizerObjectProvider,
                ObjectProvider<SupportedListener> listenerObjectProvider
        ) {
            this.customizerObjectProvider = customizerObjectProvider;
            this.listenerObjectProvider = listenerObjectProvider;
        }

        @Override
        public void addCustomizers(List<DhmpMybatisFlexCustomizer> customizerList) {
            customizerObjectProvider.orderedStream().forEach(customizerList::add);
        }

        @Override
        public void addSetListeners(List<SetListener> setListeners) {
            listenerObjectProvider.orderedStream().forEach(setListeners::add);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({LovClient.class})
    static class LovSetListenerConfiguration {

        @Bean
        public LovSetListener lovSetListener(ObjectProvider<LovClient> lovClientObjectProvider) {
            return new LovSetListener(lovClientObjectProvider);
        }
    }

    @Configuration(proxyBeanMethods = false)
    public static class EnableDhmpMybatisFlexConfiguration extends DelegatingDhmpFlexConfiguration {

        @Bean
        DhmpMybatisFlexCustomizer defaultFlexCustomizer(DelegatingSetListener setListener) {
            return new DefaultDhmpMybatisFlexCustomizer(setListener);
        }


        @Bean
        MyBatisFlexCustomizer mybatisFlexCustomizer() {
            DhmpMybatisFlexCustomizerComposite flexCustomizer = new DhmpMybatisFlexCustomizerComposite();
            flexCustomizer.setCustomizerList(getCustomizers());
            return flexCustomizer;
        }

        private List<DhmpMybatisFlexCustomizer> getCustomizers() {
            List<DhmpMybatisFlexCustomizer> customizers = new ArrayList<>();
            addCustomizers(customizers);
            return customizers;
        }
    }
}
