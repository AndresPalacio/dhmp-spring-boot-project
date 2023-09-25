package com.zznode.dhmp.boot.autoconfigure.mybatis.plus;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;

/**
 * 拓展MybatisPlus的自动配置类
 *
 * @author 王俊
 * @date create in 2023/6/30 18:01
 */
@AutoConfiguration
@AutoConfigureBefore({MybatisPlusAutoConfiguration.class})
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class, MybatisMapWrapperFactory.class})
@Import({DhmpMybatisPlusConfiguration.class, MybatisPlusInterceptorConfiguration.class})
public class DhmpMybatisPlusAutoConfiguration {

}
