package net.renfei.server.cms.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.renfei.server.cms.constant.ArticleStatusEnum;
import net.renfei.server.core.constant.SecretLevelEnum;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 文章内容对象
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "文章内容对象")
public class Article implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    @Schema(description = "文章ID")
    private Long id;
    @NotNull
    @Schema(description = "标题")
    private String title;
    @Schema(description = "关键字")
    private String keyword;
    @Schema(description = "描述")
    private String description;
    @NotNull
    @Schema(description = "分类ID")
    private Long categoryId;
    @Schema(description = "文章封面")
    private String coverImage;
    @Schema(description = "作者用户名")
    private String authorName;
    @Schema(description = "作者ID")
    private Long authorId;
    @NotNull
    @Schema(description = "发布时间")
    private Date publishDate;
    @Schema(description = "修改时间")
    private Date updateDate;
    @Schema(description = "状态")
    private ArticleStatusEnum status;
    @Schema(description = "浏览量")
    private Long views;
    @Schema(description = "评论状态：OPENED/CLOSED")
    private String commentStatus;
    @Schema(description = "密码保护")
    private String password;
    @Schema(description = "版本号")
    private Integer versions;
    @Schema(description = "保密等级")
    private SecretLevelEnum secretLevel;
    @NotNull
    @Schema(description = "是否原创")
    private Boolean isOriginal;
    @Schema(description = "公文：索引号")
    private String indexNumber;
    @Schema(description = "公文：发文机关")
    private String documentIssuingAgency;
    @Schema(description = "公文：成文日期")
    private Date writtenDate;
    @Schema(description = "公文：发文字号")
    private String issuedNumber;
    @Schema(description = "公文：公文种类")
    private String officialDocCategory;
    @Schema(description = "来源")
    private String source;
    @Schema(description = "来源链接")
    private String sourceUrl;
    @Schema(description = "点赞量")
    private Long thumbsUp;
    @Schema(description = "点踩量")
    private Long thumbsDown;
    @Schema(description = "文章预览时长")
    private String readTime;
    @Schema(description = "文章字数")
    private Long wordCount;
    @NotNull
    @Schema(description = "文章内容")
    private String content;
}
