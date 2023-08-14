package net.renfei.server.cms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import net.renfei.server.cms.entity.Tag;
import lombok.RequiredArgsConstructor;
import net.renfei.server.cms.service.TagsService;
import net.renfei.server.core.annotation.AuditLog;
import net.renfei.server.core.controller.BaseController;
import net.renfei.server.core.entity.ApiResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章标签
 *
 * @author renfei
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@io.swagger.v3.oas.annotations.tags.Tag(name = "文章标签", description = "文章标签")
public class TagsController extends BaseController {
    private final static String MODULE_NAME = "ARTICLE_TAG";
    private final TagsService tagsService;

    @Operation(summary = "查询文章标签", description = "查询文章标签",
            parameters = {
                    @Parameter(name = "enName", description = "标签英文名"),
                    @Parameter(name = "zhName", description = "标签中文名")
            })
    @GetMapping("/cms/article/tag")
    @AuditLog(module = MODULE_NAME, operation = "查询文章标签", descriptionExpression = "查询文章标签")
    public ApiResult<List<Tag>> queryTagList(String enName, String zhName) {
        return new ApiResult<>(tagsService.queryTagList(enName, zhName));
    }

    @Operation(summary = "创建新文章标签", description = "创建新文章标签")
    @PostMapping("/cms/article/tag")
    @AuditLog(module = MODULE_NAME, operation = "创建新文章标签", descriptionExpression = "创建新文章标签")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') or hasAuthority('cms:articletag:create')")
    public ApiResult<?> createTag(@RequestBody Tag tag) {
        tagsService.createTag(tag);
        return ApiResult.success();
    }

    @Operation(summary = "修改文章标签", description = "修改文章标签")
    @PutMapping("/cms/article/tag/{id}")
    @AuditLog(module = MODULE_NAME, operation = "修改文章标签", descriptionExpression = "修改文章标签")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') or hasAuthority('cms:articletag:update')")
    public ApiResult<?> updateTag(@PathVariable("id") Long id, @RequestBody Tag tag) {
        tagsService.updateTag(id, tag);
        return ApiResult.success();
    }

    @Operation(summary = "删除文章标签", description = "删除文章标签")
    @DeleteMapping("/cms/article/tag/{id}")
    @AuditLog(module = MODULE_NAME, operation = "删除文章标签", descriptionExpression = "删除文章标签")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') or hasAuthority('cms:articletag:delete')")
    public ApiResult<?> deleteTag(@PathVariable("id") Long id) {
        tagsService.deleteTag(id);
        return ApiResult.success();
    }
}
