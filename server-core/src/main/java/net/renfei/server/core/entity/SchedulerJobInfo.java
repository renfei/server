package net.renfei.server.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务信息
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "定时任务信息")
public class SchedulerJobInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    @Schema(description = "任务类名")
    private String jobClassName;
    @Schema(description = "cron表达式")
    private String cronExpression;
    @Schema(description = "参数")
    private String parameter;
    @Schema(description = "任务名称")
    private String jobName;
    @Schema(description = "任务分组")
    private String jobGroup;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "任务状态")
    private String jobStatus;
    @Schema(description = "任务开始时间")
    private Date startTime;
    @Schema(description = "任务结束时间")
    private Date endTime;
}
