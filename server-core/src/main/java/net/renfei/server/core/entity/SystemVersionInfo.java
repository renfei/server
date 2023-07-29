package net.renfei.server.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统版本信息
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "系统版本信息")
public class SystemVersionInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    @Schema(description = "代码分支")
    private String codeBranch;
    @Schema(description = "构建时间")
    private String buildTime;
    @Schema(description = "构建者邮箱")
    private String buildUserEmail;
    @Schema(description = "构建者")
    private String buildUsername;
    @Schema(description = "构建版本")
    private String buildVersion;
    @Schema(description = "提交ID")
    private String commitId;
    @Schema(description = "提交ID缩写")
    private String commitIdAbbrev;
    @Schema(description = "标签")
    private String tags;
}
