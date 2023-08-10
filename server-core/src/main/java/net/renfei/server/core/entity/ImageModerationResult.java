package net.renfei.server.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

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
public class ImageModerationResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    @Schema(description = "是否通过")
    public Boolean pass;
    @Schema(description = "检测对象对应的数据ID")
    public String dataId;
    @Schema(description = "图片检测的风险标签、置信分等参数结果")
    public List<Result> result;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        @Schema(description = "风险标签")
        private String label;
        @Schema(description = "置信分值，0到100分，保留到小数点后2位")
        private Float confidence;
    }
}
