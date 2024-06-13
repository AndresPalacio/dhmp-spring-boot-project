package com.zznode.dhmp.boot.context;

import com.zznode.dhmp.context.annotation.ProvinceComponent;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

/**
 * spring注册component时排除被{@link com.zznode.dhmp.context.annotation.ProvinceComponent ProvinceComponent}标记的组件
 * 排除后的组件将由{@link com.zznode.dhmp.context.ProvinceComponentScanner ProvinceComponentScanner}重新扫描注册.
 * <p>此过滤器需要在扫描包之前注入到beanFactory中
 *
 * @author 王俊
 * @date create in 2023/5/15 10:59
 */
public final class ProvinceComponentExcludeFilter extends TypeExcludeFilter {

    private static final String BEAN_NAME = ProvinceComponentExcludeFilter.class.getName();

    private static final ProvinceComponentExcludeFilter INSTANCE = new ProvinceComponentExcludeFilter();

    private static final String ANNOTATION_NAME = ProvinceComponent.class.getName();

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) {
        AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
        return (metadata.isAnnotated(ANNOTATION_NAME));
    }

    static void registerWith(ConfigurableListableBeanFactory beanFactory) {
        if (!beanFactory.containsSingleton(BEAN_NAME)) {
            beanFactory.registerSingleton(BEAN_NAME, INSTANCE);
        }
    }
}
