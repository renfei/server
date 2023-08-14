package net.renfei.server.cms.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文章内容标签对象
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "文章内容标签对象")
public class Tag implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    @Schema(description = "标签ID")
    private Long id;
    @Schema(description = "标签英文名")
    private String tagEnName;
    @Schema(description = "标签中文名")
    private String tagZhName;
    @Schema(description = "描述")
    private String description;
}
