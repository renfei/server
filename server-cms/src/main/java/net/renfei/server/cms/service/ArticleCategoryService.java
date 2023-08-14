package net.renfei.server.cms.service;

import net.renfei.server.cms.entity.ArticleCategory;

import java.util.List;

/**
 * 文章内容分类服务
 *
 * @author renfei
 */
public interface ArticleCategoryService {
    /**
     * 查询文章内容分类树
     *
     * @param pid 父级ID
     * @return
     */
    List<ArticleCategory> queryArticleCategoryTree(long pid);

    /**
     * 按英文名称查找文章内容分类
     *
     * @param enName 英文名称
     * @return
     */
    ArticleCategory queryArticleCategoryByName(String enName);

    /**
     * ID查找文章内容分类
     *
     * @param id 文章内容分类ID
     * @return
     */
    ArticleCategory queryArticleCategoryById(Long id);

    /**
     * 查询全部子级文章内容分类ID
     *
     * @param id 文章内容分类ID
     * @return
     */
    List<Long> queryAllChildCategoryId(Long id);

    /**
     * 创建文章内容分类
     *
     * @param articleCategory 文章内容分类
     */
    void createArticleCategory(ArticleCategory articleCategory);

    /**
     * 修改文章内容分类
     *
     * @param id              文章内容分类ID
     * @param articleCategory 文章内容分类
     */
    void updateArticleCategory(Long id, ArticleCategory articleCategory);

    /**
     * 删除文章内容分类
     *
     * @param id 文章内容分类ID
     */
    void deleteArticleCategory(Long id);
}
