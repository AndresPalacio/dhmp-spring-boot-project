package com.zznode.dhmp.boot.autoconfigure.cache;

import com.zznode.dhmp.lov.LovCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * 描述
 *
 * @author 王俊
 * @date create in 2023/9/1
 */
@Configuration
@ConditionalOnClass(LovCache.class)
public class LovCacheConfiguration {

}
