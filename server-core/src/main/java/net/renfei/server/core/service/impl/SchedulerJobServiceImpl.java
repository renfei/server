package net.renfei.server.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.entity.SchedulerJobInfo;
import net.renfei.server.core.service.BaseService;
import net.renfei.server.core.service.SchedulerJobService;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.MutableTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

import static org.quartz.Trigger.TriggerState.*;

/**
 * 定时任务服务
 *
 * @author renfei
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerJobServiceImpl extends BaseService implements SchedulerJobService {
    private final static Map<Trigger.TriggerState, String> TRIGGER_STATE = new HashMap<>() {{
        this.put(NONE, "空");
        this.put(NORMAL, "正常");
        this.put(PAUSED, "暂停");
        this.put(COMPLETE, "完成");
        this.put(ERROR, "错误");
        this.put(BLOCKED, "阻塞");
    }};
    private final Scheduler scheduler;

    @Override
    public List<SchedulerJobInfo> querySchedulerJob(String jobGroup) throws SchedulerException {
        GroupMatcher<JobKey> matcher;
        if (StringUtils.hasLength(jobGroup)) {
            matcher = GroupMatcher.groupEquals(jobGroup);
        } else {
            matcher = GroupMatcher.anyGroup();
        }
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<SchedulerJobInfo> schedulerJobInfos = new ArrayList<>(jobKeys.size());
        for (JobKey jobKey : jobKeys) {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup());
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            JobDataMap jobDataMap = scheduler.getJobDetail(jobKey).getJobDataMap();
            ObjectMapper objectMapper = new ObjectMapper();
            SchedulerJobInfo.SchedulerJobInfoBuilder builder = SchedulerJobInfo.builder()
                    .jobName(jobKey.getName())
                    .jobGroup(jobKey.getGroup())
                    .jobStatus(this.getJobStatus(triggerKey));
            try {
                builder.parameter(objectMapper.writeValueAsString(jobDataMap));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
            if (!jobKey.getGroup().contains(END_JOB_SUFFIX)) {
                builder.jobClassName(cronTrigger.getJobDataMap().getString("jobClassName"))
                        .description(cronTrigger.getDescription())
                        .cronExpression(cronTrigger.getCronExpression())
                        .startTime(cronTrigger.getStartTime());
            }
            TriggerKey endJobTriggerKey = TriggerKey.triggerKey(jobKey.getName(), jobKey.getName() + END_JOB_SUFFIX);
            SimpleTrigger endJobTrigger = (SimpleTrigger) scheduler.getTrigger(endJobTriggerKey);
            if (endJobTrigger != null) {
                builder.endTime(endJobTrigger.getStartTime());
            }
            schedulerJobInfos.add(builder.build());
        }
        return schedulerJobInfos;
    }

    @Override
    public void createScheduleJob(SchedulerJobInfo jobInfo) throws ClassNotFoundException, JsonProcessingException, SchedulerException {
        //获取定时任务的执行类，必须是类的绝对路径
        //定时任务类需要是job类的具体实现 QuartzJobBean是job的抽象类。
        Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(jobInfo.getJobClassName());
        //构建定时任务信息
        JobKey jobKey = JobKey.jobKey(jobInfo.getJobName(), jobInfo.getJobGroup());
        TriggerKey triggerKey = TriggerKey.triggerKey(jobInfo.getJobName(), jobInfo.getJobGroup());
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobKey).build();
        //设置定时任务的执行方式
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfo.getCronExpression());
        //构建触发器trigger
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withDescription(jobInfo.getDescription())
                .usingJobData("jobClassName", jobInfo.getJobClassName())
                .withSchedule(scheduleBuilder)
                .startAt(jobInfo.getStartTime())
                //如果设置了结束时间，到期任务自动删除，我们不想任务过期就删除，所以zhudiao
                //.endAt(jobInfo.getEndTime())
                .build();
        // 修改 misfire 策略,如果开始时间在添加任务之前，不修改策略会导致新增时任务会立即运行一次
        chgMisFire(trigger);
        //如果参数不为空，写入参数
        ObjectMapper mapper = new ObjectMapper();
        if (StringUtils.hasLength(jobInfo.getParameter())) {
            Map<String, Object> param = mapper.readValue(jobInfo.getParameter(), new TypeReference<>() {
            });
            jobDetail.getJobDataMap().putAll(param);
        }
        scheduler.scheduleJob(jobDetail, trigger);
        //如果设置了结束时间，添加一个终止任务
        if (null != jobInfo.getEndTime()) {
            this.addEndJob(jobInfo);
        }
    }

    /**
     * 添加停止任务
     *
     * @param jobInfo 定时任务信息
     * @throws JsonProcessingException
     * @throws SchedulerException
     * @throws ClassNotFoundException
     */
    public void addEndJob(SchedulerJobInfo jobInfo) throws JsonProcessingException, SchedulerException, ClassNotFoundException {
        String jobName = jobInfo.getJobName();
        String group = jobName + END_JOB_SUFFIX;
        JobKey jobKey = JobKey.jobKey(jobName, group);
        String endJobName = "jobName-" + System.currentTimeMillis();
        TriggerKey triggerKey = TriggerKey.triggerKey(endJobName, group);
        if (scheduler.checkExists(triggerKey) && scheduler.checkExists(jobKey)) {
            log.info("{}----开始添加终止任务", jobName);
            SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withDescription(jobInfo.getDescription() + "-[结束任务]")
                    .withIdentity(triggerKey)
                    .startAt(jobInfo.getEndTime()).build();
            // 修改 misfire 策略
            chgMisFire(trigger);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            ObjectMapper mapper = new ObjectMapper();
            if (StringUtils.hasLength(jobInfo.getParameter())) {
                Map<String, Object> param = mapper.readValue(jobInfo.getParameter(), new TypeReference<>() {
                });
                jobDetail.getJobDataMap().putAll(param);
            }
            scheduler.deleteJob(jobKey);
            scheduler.scheduleJob(jobDetail, trigger);
            return;
        }
        Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(jobInfo.getJobClassName());
        //构建定时任务信息
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobKey).build();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
                .withDescription(jobKey.getGroup() + "-[结束任务]")
                .startAt(jobInfo.getEndTime())
                .build();
        // 修改 misfire 策略
        chgMisFire(trigger);

        ObjectMapper mapper = new ObjectMapper();
        if (StringUtils.hasLength(jobInfo.getParameter())) {
            log.info("parameter:{}", jobInfo.getParameter());
            Map<String, Object> param = mapper.readValue(jobInfo.getParameter(), new TypeReference<>() {
            });
            jobDetail.getJobDataMap().putAll(param);
        }
        scheduler.scheduleJob(jobDetail, trigger);
    }

    @Override
    public void pauseScheduleJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.pauseJob(jobKey);
    }

    @Override
    public void resumeScheduleJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.resumeJob(jobKey);
    }

    @Override
    public void runOnce(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.triggerJob(jobKey);
    }

    @Override
    public void updateScheduleJob(SchedulerJobInfo jobInfo) throws SchedulerException, JsonProcessingException, ClassNotFoundException {
        //获取对应任务的触发器
        TriggerKey triggerKey = TriggerKey.triggerKey(jobInfo.getJobName(), jobInfo.getJobGroup());
        //设置定时任务执行方式
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfo.getCronExpression());
        //重新构建定时任务的触发器
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        cronTrigger = cronTrigger.getTriggerBuilder()
                .withIdentity(triggerKey)
                .withSchedule(scheduleBuilder)
                .withDescription(jobInfo.getDescription())
                .usingJobData("jobClassName", jobInfo.getJobClassName())
                .startAt(jobInfo.getStartTime())
                .endAt(jobInfo.getEndTime()).build();
        // 修改 misfire 策略,测试时注掉，方便观察
        //chgMisFire(trigger);
        //重置对应的定时任务
        //如果参数不为空，写入参数
        JobKey jobKey = JobKey.jobKey(jobInfo.getJobName(), jobInfo.getJobGroup());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        ObjectMapper mapper = new ObjectMapper();
        if (StringUtils.hasLength(jobInfo.getParameter())) {
            Map<String, Object> param = mapper.readValue(jobInfo.getParameter(), new TypeReference<>() {
            });
            jobDetail.getJobDataMap().clear();
            jobDetail.getJobDataMap().putAll(param);
        }
        scheduler.rescheduleJob(triggerKey, cronTrigger);
        //如果设置了结束时间，添加一个终止任务
        if (null != jobInfo.getEndTime()) {
            this.addEndJob(jobInfo);
        }
    }

    @Override
    public void deleteScheduleJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.deleteJob(jobKey);
    }

    /**
     * 获取定时任务状态
     *
     * @param triggerKey 触发器
     * @return
     * @throws SchedulerException
     */
    private String getJobStatus(TriggerKey triggerKey) throws SchedulerException {
        Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
        return TRIGGER_STATE.get(triggerState);
    }

    /**
     * 错过触发策略：即错过了定时任务的开始时间可以做什么，此处设置什么也不做，即过了开始时间也不执行
     * 如果不设置，默认为立即触发一次，例如设置定时任务开始时间为10点，没有设置结束时间，在10点后重启系统，
     * 如果不设置为CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING，那么开始时间在重启前的定时任务会全部重新触发一次
     *
     * @param trigger
     */
    private void chgMisFire(Trigger trigger) {
        // 修改 misfire 策略
        if (trigger instanceof MutableTrigger mutableTrigger) {
            mutableTrigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
        }
    }
}
