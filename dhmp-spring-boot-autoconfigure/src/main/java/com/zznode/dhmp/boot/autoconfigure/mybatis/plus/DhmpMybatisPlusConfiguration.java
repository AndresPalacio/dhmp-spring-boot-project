package com.zznode.dhmp.boot.autoconfigure.mybatis.plus;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus配置
 *
 * @author 王俊
 * @date create in 2023/6/30 18:02
 */
@Configuration
public class DhmpMybatisPlusConfiguration {

    @Bean
    MybatisPlusPropertiesCustomizer mybatisPlusPropertiesCustomizer() {
        // 不让打mybatis-plus的banner
        return (properties) -> properties.getGlobalConfig().setBanner(false);
    }


}
