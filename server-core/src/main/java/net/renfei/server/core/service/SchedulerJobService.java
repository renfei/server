package net.renfei.server.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.renfei.server.core.entity.SchedulerJobInfo;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * 定时任务服务
 *
 * @author renfei
 */
public interface SchedulerJobService {
    String END_JOB_SUFFIX = ":endTask";

    /**
     * 查询所有定时任务
     *
     * @param jobGroup 任务组名
     * @return
     * @throws SchedulerException
     */
    List<SchedulerJobInfo> querySchedulerJob(String jobGroup) throws SchedulerException;

    /**
     * 创建定时任务，创建定时任务后默认为启动状态
     *
     * @param jobInfo 定时任务信息
     * @throws ClassNotFoundException
     * @throws JsonProcessingException
     * @throws SchedulerException
     */
    void createScheduleJob(SchedulerJobInfo jobInfo) throws ClassNotFoundException, JsonProcessingException, SchedulerException;

    /**
     * 根据任务明细暂停定时任务
     *
     * @param jobName  定时任务名称
     * @param jobGroup 定时任务组名
     * @throws SchedulerException
     */
    void pauseScheduleJob(String jobName, String jobGroup) throws SchedulerException;

    /**
     * 根据任务名称恢复定时任务
     *
     * @param jobName  定时任务名称
     * @param jobGroup 定时任务组名
     * @throws SchedulerException
     */
    void resumeScheduleJob(String jobName, String jobGroup) throws SchedulerException;

    /**
     * 根据任务名称运行一次定时任务
     *
     * @param jobName  定时任务名称
     * @param jobGroup 定时任务组名
     * @throws SchedulerException
     */
    void runOnce(String jobName, String jobGroup) throws SchedulerException;

    /**
     * 更新定时任务
     *
     * @param jobInfo 定时任务类
     * @throws SchedulerException
     * @throws JsonProcessingException
     * @throws ClassNotFoundException
     */
    void updateScheduleJob(SchedulerJobInfo jobInfo) throws SchedulerException, JsonProcessingException, ClassNotFoundException;

    /**
     * 删除定时任务
     *
     * @param jobName  定时任务名称
     * @param jobGroup 定时任务组名
     * @throws SchedulerException
     */
    void deleteScheduleJob(String jobName, String jobGroup) throws SchedulerException;
}
