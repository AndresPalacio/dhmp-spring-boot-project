package com.zznode.dhmp.boot.autoconfigure.data.page;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInterceptor;
import com.github.pagehelper.autoconfigure.PageHelperProperties;
import com.github.pagehelper.autoconfigure.PageHelperStandardProperties;
import com.zznode.dhmp.boot.autoconfigure.mybatis.flex.DhmpMybatisFlexAutoConfiguration;
import com.zznode.dhmp.boot.autoconfigure.mybatis.plus.DhmpMybatisPlusAutoConfiguration;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.List;

/**
 * 重写com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration
 * <p>
 * 官方的PageHelper自动配置依赖了MybatisAutoConfiguration，
 * 而有些使用的mybatis增强框架在依赖MybatisAutoConfiguration后会出问题，所以需要将MybatisAutoConfiguration排除掉，
 * 导致官方的PageHelperAutoConfiguration会排在最前面，从而获取不到SqlSessionFactory,就无法将PageInterceptor添加到mybatis中去。
 * <p>
 *
 * @author 王俊
 * @see com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration
 */
@ConditionalOnClass(PageHelper.class)
@ConditionalOnBean(SqlSessionFactory.class)
@EnableConfigurationProperties({PageHelperProperties.class, PageHelperStandardProperties.class})
@AutoConfigureAfter({DhmpMybatisFlexAutoConfiguration.class, DhmpMybatisPlusAutoConfiguration.class})
public class PageHelperAutoConfiguration implements InitializingBean {
    private final List<SqlSessionFactory> sqlSessionFactoryList;

    private final PageHelperProperties properties;

    public PageHelperAutoConfiguration(List<SqlSessionFactory> sqlSessionFactoryList, PageHelperStandardProperties standardProperties) {
        this.sqlSessionFactoryList = sqlSessionFactoryList;
        this.properties = standardProperties.getProperties();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        PageInterceptor interceptor = new PageInterceptor();
        interceptor.setProperties(this.properties);
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            if (!containsInterceptor(configuration, interceptor)) {
                configuration.addInterceptor(interceptor);
            }
        }
    }

    /**
     * 是否已经存在相同拦截器
     */
    private boolean containsInterceptor(org.apache.ibatis.session.Configuration configuration, Interceptor interceptor) {
        try {
            // getInterceptors since 3.2.2
            return configuration.getInterceptors().stream().anyMatch(config -> interceptor.getClass().isAssignableFrom(config.getClass()));
        } catch (Exception e) {
            return false;
        }
    }

}
