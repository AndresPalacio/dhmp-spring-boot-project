package com.zznode.dhmp.boot.autoconfigure.lov;

import com.zznode.dhmp.lov.LovCache;
import com.zznode.dhmp.lov.LovManager;
import com.zznode.dhmp.lov.client.DefaultLovClient;
import com.zznode.dhmp.lov.client.LovClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * lov自动配置类
 *
 * @author 王俊
 * @date create in 2023/9/1
 */
@AutoConfiguration(after = CacheAutoConfiguration.class)
@Import({MybatisLovManagerConfiguration.class})
@ConditionalOnClass({DefaultLovClient.class, CacheManager.class})
public class LovAutoConfiguration {

    @Bean
    public LovClient lovClient(ObjectProvider<LovCache> lovCache,
                               LovManager lovManager) {
        DefaultLovClient lovClient = new DefaultLovClient();
        lovCache.ifAvailable(lovClient::setLovCache);
        lovClient.setLovManager(lovManager);
        return lovClient;
    }

}
