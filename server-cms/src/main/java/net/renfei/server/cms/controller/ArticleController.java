package net.renfei.server.cms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.renfei.server.cms.constant.ArticleStatusEnum;
import net.renfei.server.cms.entity.Article;
import net.renfei.server.cms.service.ArticleService;
import net.renfei.server.core.annotation.AuditLog;
import net.renfei.server.core.constant.SecretLevelEnum;
import net.renfei.server.core.controller.BaseController;
import net.renfei.server.core.entity.ApiResult;
import net.renfei.server.core.entity.ListData;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 文章内容
 *
 * @author renfei
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "文章内容", description = "文章内容")
public class ArticleController extends BaseController {
    private final static String MODULE_NAME = "ARTICLE";
    private final ArticleService articleService;

    @Operation(summary = "查询审计日志", description = "查询审计日志",
            parameters = {
                    @Parameter(name = "id", description = "文章ID"),
                    @Parameter(name = "title", description = "文章标题"),
                    @Parameter(name = "categoryId", description = "分类ID"),
                    @Parameter(name = "authorName", description = "作者名称"),
                    @Parameter(name = "publishDateMin", description = "开始时间"),
                    @Parameter(name = "publishDateMax", description = "结束时间"),
                    @Parameter(name = "status", description = "状态"),
                    @Parameter(name = "secretLevel", description = "密级"),
                    @Parameter(name = "pages", description = "页码"),
                    @Parameter(name = "rows", description = "每页容量")
            })
    @GetMapping("/cms/article")
    @AuditLog(module = MODULE_NAME, operation = "查询文章列表", descriptionExpression = "查询文章列表")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') or hasAuthority('cms:article:query')")
    public ApiResult<ListData<Article>> queryArticleList(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "authorName", required = false) String authorName,
            @RequestParam(value = "publishDateMin", required = false) Date publishDateMin,
            @RequestParam(value = "publishDateMax", required = false) Date publishDateMax,
            @RequestParam(value = "status", required = false) ArticleStatusEnum status,
            @RequestParam(value = "secretLevel", required = false) SecretLevelEnum secretLevel,
            @RequestParam(value = "pages", required = false, defaultValue = "1") int pages,
            @RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
        return new ApiResult<>(articleService.queryArticleList(id, title, categoryId, authorName,
                publishDateMin, publishDateMax, status, secretLevel, pages, rows));
    }

    @Operation(summary = "创建新文章", description = "创建新文章")
    @PostMapping("/cms/article")
    @AuditLog(module = MODULE_NAME, operation = "创建新文章", descriptionExpression = "创建新文章")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') or hasAuthority('cms:article:create')")
    public ApiResult<Article> createArticle(@RequestBody Article article) {
        return new ApiResult<>(articleService.createArticle(article));
    }

    @Operation(summary = "修改文章", description = "修改文章，注意会使文章重新进入审核状态，需要重新上线")
    @PutMapping("/cms/article/{id}")
    @AuditLog(module = MODULE_NAME, operation = "修改文章", descriptionExpression = "修改文章")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') or hasAuthority('cms:article:update')")
    public ApiResult<?> updateArticle(@PathVariable("id") Long id, @RequestBody Article article) {
        articleService.updateArticle(id, article);
        return ApiResult.success();
    }

    @Operation(summary = "发布/上线文章", description = "发布/上线文章，文章将被公开")
    @PutMapping("/cms/article/{id}/publish")
    @AuditLog(module = MODULE_NAME, operation = "发布/上线文章", descriptionExpression = "发布/上线文章")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') or hasAuthority('cms:article:publish')")
    public ApiResult<?> publishArticle(@PathVariable("id") Long id) {
        articleService.publishArticle(id);
        return ApiResult.success();
    }

    @Operation(summary = "下线撤回文章", description = "下线撤回文章")
    @PutMapping("/cms/article/{id}/offline")
    @AuditLog(module = MODULE_NAME, operation = "下线撤回文章", descriptionExpression = "下线撤回文章")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') or hasAuthority('cms:article:offline')")
    public ApiResult<?> offlineArticle(@PathVariable("id") Long id) {
        articleService.offlineArticle(id);
        return ApiResult.success();
    }

    @Operation(summary = "删除文章", description = "删除文章")
    @DeleteMapping("/cms/article/{id}")
    @AuditLog(module = MODULE_NAME, operation = "删除文章", descriptionExpression = "删除文章")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') or hasAuthority('cms:article:delete')")
    public ApiResult<?> deleteArticle(@PathVariable("id") Long id) {
        articleService.deleteArticle(id);
        return ApiResult.success();
    }

    @Operation(summary = "给文章定密", description = "给文章定密")
    @PutMapping("/cms/article/{id}/secret")
    @AuditLog(module = MODULE_NAME, operation = "给文章定密", descriptionExpression = "给文章定密")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER') or hasAuthority('cms:article:secret')")
    public ApiResult<?> settingArticleSecretLevel(@PathVariable("id") Long id,
                                                  @RequestParam("secretLevel") SecretLevelEnum secretLevel) {
        articleService.settingArticleSecretLevel(id, secretLevel);
        return ApiResult.success();
    }
}
