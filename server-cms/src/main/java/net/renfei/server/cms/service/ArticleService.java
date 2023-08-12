package net.renfei.server.cms.service;

import net.renfei.server.cms.constant.ArticleStatusEnum;
import net.renfei.server.cms.entity.Article;
import net.renfei.server.core.constant.SecretLevelEnum;
import net.renfei.server.core.entity.ListData;

import java.util.Date;

/**
 * 文章服务
 *
 * @author renfei
 */
public interface ArticleService {
    /**
     * 查询文章列表
     *
     * @param id             文章ID
     * @param title          文章标题
     * @param categoryId     分类ID
     * @param authorName     作者名称
     * @param publishDateMin 发布时间起始
     * @param publishDateMax 发布时间结束
     * @param status         状态
     * @param secretLevel    密级
     * @param pages          页码
     * @param rows           每页容量
     * @return
     */
    ListData<Article> queryArticleList(Long id, String title, Long categoryId, String authorName,
                                       Date publishDateMin, Date publishDateMax, ArticleStatusEnum status,
                                       SecretLevelEnum secretLevel, int pages, int rows);

    /**
     * 创建新文章
     *
     * @param article
     * @return
     */
    Article createArticle(Article article);

    /**
     * 修改文章
     *
     * @param id      文章ID
     * @param article 文章内容
     */
    void updateArticle(Long id, Article article);

    /**
     * 发布文章
     *
     * @param id 文章ID
     */
    void publishArticle(Long id);

    /**
     * 下线撤回文章
     *
     * @param id 文章ID
     */
    void offlineArticle(Long id);

    /**
     * 删除文章
     *
     * @param id 文章ID
     */
    void deleteArticle(Long id);

    /**
     * 给文章定密
     *
     * @param id          文章ID
     * @param secretLevel 密级
     */
    void settingArticleSecretLevel(Long id, SecretLevelEnum secretLevel);

    /**
     * 根据内容估算阅读时间
     *
     * @param text 内容
     * @return
     */
    String getReadTime(String text);

    /**
     * 获取内容的字数
     *
     * @param text 内容
     * @return
     */
    long getWordCount(String text);

    /**
     * 获取HTML中的文本内容
     *
     * @param html HTML代码
     * @return
     */
    String getPlainText(String html);
}
