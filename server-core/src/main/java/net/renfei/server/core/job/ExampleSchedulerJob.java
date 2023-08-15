package net.renfei.server.core.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 演示定时任务
 *
 * @author renfei
 */
@Slf4j
@Component
public class ExampleSchedulerJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 定时任务的逻辑在此实现，下方只是演示
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        //获取参数信息
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        String name = jobDetail.getKey().getName();
        String jobGroup = jobDetail.getKey().getGroup();
        for (Map.Entry<String, Object> entry : jobDataMap.entrySet()) {
            log.info("定时任务-{}:{}-启动，参数为：key：{} ，value：{}", name, jobGroup, entry.getKey(), entry.getValue());
        }
        Trigger cronTrigger = jobExecutionContext.getTrigger();
        String group = cronTrigger.getKey().getGroup();
    }
}
