package net.renfei.server.cms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.renfei.server.cms.entity.ArticleCategory;
import net.renfei.server.cms.service.ArticleCategoryService;
import net.renfei.server.core.annotation.AuditLog;
import net.renfei.server.core.controller.BaseController;
import net.renfei.server.core.entity.ApiResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章内容分类
 *
 * @author renfei
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "文章内容分类", description = "文章内容分类")
public class ArticleCategoryController extends BaseController {
    private final static String MODULE_NAME = "ARTICLE_CATEGORY";
    private final ArticleCategoryService categoryService;

    @Operation(summary = "查询文章内容分类树", description = "查询文章内容分类树",
            parameters = {
                    @Parameter(name = "pid", description = "父级ID")
            })
    @GetMapping("/cms/article/category/tree")
    @AuditLog(module = MODULE_NAME, operation = "查询文章内容分类树", descriptionExpression = "查询文章内容分类树")
    public ApiResult<List<ArticleCategory>> queryArticleCategoryTree(
            @RequestParam(value = "pid", required = false, defaultValue = "0") Long pid) {
        return new ApiResult<>(categoryService.queryArticleCategoryTree(pid));
    }

    @Operation(summary = "创建新文章内容分类", description = "创建新文章内容分类")
    @PostMapping("/cms/article/category")
    @AuditLog(module = MODULE_NAME, operation = "创建新文章内容分类", descriptionExpression = "创建新文章内容分类")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') or hasAuthority('cms:articlecategory:create')")
    public ApiResult<?> createArticleCategory(@RequestBody ArticleCategory articleCategory) {
        categoryService.createArticleCategory(articleCategory);
        return ApiResult.success();
    }

    @Operation(summary = "修改文章内容分类", description = "修改文章内容分类")
    @PutMapping("/cms/article/category/{id}")
    @AuditLog(module = MODULE_NAME, operation = "修改文章内容分类", descriptionExpression = "修改文章内容分类")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') or hasAuthority('cms:articlecategory:update')")
    public ApiResult<?> updateArticleCategory(@PathVariable("id") Long id,
                                              @RequestBody ArticleCategory articleCategory) {
        categoryService.updateArticleCategory(id, articleCategory);
        return ApiResult.success();
    }

    @Operation(summary = "删除文章内容分类", description = "删除文章内容分类")
    @DeleteMapping("/cms/article/category/{id}")
    @AuditLog(module = MODULE_NAME, operation = "删除文章内容分类", descriptionExpression = "删除文章内容分类")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') or hasAuthority('cms:articlecategory:delete')")
    public ApiResult<?> deleteArticleCategory(@PathVariable("id") Long id) {
        categoryService.deleteArticleCategory(id);
        return ApiResult.success();
    }
}
