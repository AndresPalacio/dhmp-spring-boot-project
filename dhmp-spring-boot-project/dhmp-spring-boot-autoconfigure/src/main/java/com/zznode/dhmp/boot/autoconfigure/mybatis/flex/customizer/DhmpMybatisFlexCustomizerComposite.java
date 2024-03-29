package com.zznode.dhmp.boot.autoconfigure.mybatis.flex.customizer;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import com.zznode.dhmp.mybatis.flex.config.customizer.DhmpMybatisFlexCustomizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 复合MyBatisFlexCustomizer,允许定义多个个性化配置
 *
 * @author 王俊
 */
public class DhmpMybatisFlexCustomizerComposite implements MyBatisFlexCustomizer {


    private List<DhmpMybatisFlexCustomizer> customizerList = new ArrayList<>();

    public void setCustomizerList(List<DhmpMybatisFlexCustomizer> customizerList) {
        this.customizerList = customizerList;
    }

    /**
     * 自定义 MyBatis-Flex 配置。
     *
     * @param globalConfig 全局配置
     */
    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        Collections.sort(customizerList);
        for (DhmpMybatisFlexCustomizer customizer : customizerList) {
            customizer.customize(globalConfig);
        }
    }
}
