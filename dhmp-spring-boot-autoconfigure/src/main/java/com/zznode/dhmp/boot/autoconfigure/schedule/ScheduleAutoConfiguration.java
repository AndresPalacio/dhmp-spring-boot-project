package com.zznode.dhmp.boot.autoconfigure.schedule;

import com.zznode.dhmp.schedule.job.DelegateExecuteJob;
import com.zznode.dhmp.schedule.manage.JobRecordManager;
import com.zznode.dhmp.schedule.manage.RemoteJobRecordManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

/**
 * 描述
 *
 * @author 王俊
 */
@AutoConfiguration(after = {RestClientAutoConfiguration.class}, before = {QuartzAutoConfiguration.class})
@ConditionalOnClass({DelegateExecuteJob.class})
public class ScheduleAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RestClient.Builder.class)
    public JobRecordManager jobRecordManager(RestClient.Builder builder) {
        return new RemoteJobRecordManager(builder.build());
    }

}
