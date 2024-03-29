package com.zznode.dhmp.boot.autoconfigure.cache;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * 描述
 *
 * @author 王俊
 * @date create in 2023/9/1
 */
@AutoConfiguration(before = CacheAutoConfiguration.class)
@Import({
        LovCacheConfiguration.class
})
public class DhmpCacheAutoConfiguration {

}
