package net.renfei.server.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 内容安全审核结果
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "内容安全审核结果")
public class TextModerationResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    @Schema(description = "是否通过")
    public Boolean pass;
    @Schema(description = "标签")
    public String labels;
    @Schema(description = "原因")
    public Reason reason;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Reason{
        @Schema(description = "细分标签")
        private String riskTips;
        @Schema(description = "命中风险片段")
        private String riskWords;
        @Schema(description = "命中广告号")
        private String adNums;
        @Schema(description = "命中用户词")
        private String customizedWords;
        @Schema(description = "命中用户词库名")
        private String customizedLibs;
    }
}
