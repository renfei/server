package net.renfei.server.core.controller.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.renfei.server.core.annotation.AuditLog;
import net.renfei.server.core.controller.BaseController;
import net.renfei.server.core.entity.ApiResult;
import net.renfei.server.core.entity.SchedulerJobInfo;
import net.renfei.server.core.service.SchedulerJobService;
import org.quartz.SchedulerException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定时任务
 *
 * @author renfei
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "定时任务", description = "定时任务")
public class SchedulerJobController extends BaseController {
    private final static String MODULE_NAME = "SCHEDULER_JOB";
    private final SchedulerJobService schedulerJobService;

    @Operation(summary = "查询定时任务列表", description = "查询定时任务列表")
    @GetMapping("/core/system/scheduler")
    @AuditLog(module = MODULE_NAME, operation = "查询定时任务列表", descriptionExpression = "查询定时任务列表")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') and hasAuthority('system:scheduler:query')")
    public ApiResult<List<SchedulerJobInfo>> querySchedulerJob(
            @RequestParam(value = "jobGroup", required = false) String jobGroup) {
        try {
            return new ApiResult<>(schedulerJobService.querySchedulerJob(jobGroup));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "创建定时任务", description = "创建定时任务")
    @PostMapping("/core/system/scheduler")
    @AuditLog(module = MODULE_NAME, operation = "创建定时任务", descriptionExpression = "创建定时任务")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') and hasAuthority('system:scheduler:create')")
    public ApiResult<?> createScheduleJob(@RequestBody SchedulerJobInfo jobInfo) {
        try {
            schedulerJobService.createScheduleJob(jobInfo);
            return ApiResult.success();
        } catch (ClassNotFoundException | JsonProcessingException | SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "修改定时任务", description = "修改定时任务")
    @PutMapping("/core/system/scheduler")
    @AuditLog(module = MODULE_NAME, operation = "修改定时任务", descriptionExpression = "修改定时任务")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') and hasAuthority('system:scheduler:update')")
    public ApiResult<?> updateScheduleJob(@RequestBody SchedulerJobInfo jobInfo) {
        try {
            schedulerJobService.updateScheduleJob(jobInfo);
            return ApiResult.success();
        } catch (ClassNotFoundException | JsonProcessingException | SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "删除定时任务", description = "删除定时任务")
    @DeleteMapping("/core/system/scheduler/{jobGroup}/{jobName}")
    @AuditLog(module = MODULE_NAME, operation = "删除定时任务",
            descriptionExpression = "删除定时任务[任务名：#{[0]}，任务组名：#{[1]}]")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') and hasAuthority('system:scheduler:delete')")
    public ApiResult<?> deleteScheduleJob(@PathVariable("jobName") String jobName,
                                          @PathVariable("jobGroup") String jobGroup) {
        try {
            schedulerJobService.deleteScheduleJob(jobName, jobGroup);
            return ApiResult.success();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "暂停定时任务", description = "暂停定时任务")
    @PutMapping("/core/system/scheduler/{jobGroup}/{jobName}/pause")
    @AuditLog(module = MODULE_NAME, operation = "暂停定时任务",
            descriptionExpression = "暂停定时任务[任务名：#{[0]}，任务组名：#{[1]}]")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') and hasAuthority('system:scheduler:pause')")
    public ApiResult<?> pauseScheduleJob(@PathVariable("jobName") String jobName,
                                         @PathVariable("jobGroup") String jobGroup) {
        try {
            schedulerJobService.pauseScheduleJob(jobName, jobGroup);
            return ApiResult.success();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "恢复定时任务", description = "恢复定时任务")
    @PutMapping("/core/system/scheduler/{jobGroup}/{jobName}/resume")
    @AuditLog(module = MODULE_NAME, operation = "恢复定时任务",
            descriptionExpression = "恢复定时任务[任务名：#{[0]}，任务组名：#{[1]}]")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') and hasAuthority('system:scheduler:resume')")
    public ApiResult<?> resumeScheduleJob(@PathVariable("jobName") String jobName,
                                          @PathVariable("jobGroup") String jobGroup) {
        try {
            schedulerJobService.resumeScheduleJob(jobName, jobGroup);
            return ApiResult.success();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "手动执行一次定时任务", description = "手动执行一次定时任务")
    @PutMapping("/core/system/scheduler/{jobGroup}/{jobName}/run")
    @AuditLog(module = MODULE_NAME, operation = "手动执行一次定时任务",
            descriptionExpression = "手动执行一次定时任务[任务名：#{[0]}，任务组名：#{[1]}]")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') and hasAuthority('system:scheduler:run')")
    public ApiResult<?> runOnce(@PathVariable("jobName") String jobName,
                                @PathVariable("jobGroup") String jobGroup) {
        try {
            schedulerJobService.runOnce(jobName, jobGroup);
            return ApiResult.success();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
