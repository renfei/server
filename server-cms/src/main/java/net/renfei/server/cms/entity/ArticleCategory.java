package net.renfei.server.cms.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 文章分类对象
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "文章分类对象")
public class ArticleCategory implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    @Schema(description = "分类ID")
    private Long id;
    @Schema(description = "父级ID")
    private Long pid;
    @NotNull
    @Schema(description = "分类英文名")
    private String categoryEnName;
    @NotNull
    @Schema(description = "分类中文名")
    private String categoryZhName;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "子级文章分类")
    private List<ArticleCategory> child;
}
