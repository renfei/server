package net.renfei.server.cms.service;

import net.renfei.server.cms.entity.Tag;

import java.util.List;

/**
 * 标签服务
 *
 * @author renfei
 */
public interface TagsService {
    /**
     * 查询标签列表
     *
     * @param enName 英文名
     * @param zhName 中文名
     * @return
     */
    List<Tag> queryTagList(String enName, String zhName);

    /**
     * 创建标签
     *
     * @param tag 标签
     */
    void createTag(Tag tag);

    /**
     * 修改标签
     *
     * @param id  标签ID
     * @param tag 标签
     */
    void updateTag(Long id, Tag tag);

    /**
     * 删除标签
     *
     * @param id 标签ID
     */
    void deleteTag(Long id);

    /**
     * 保存文章内容与标签的关系
     *
     * @param articleId 文章内容ID
     * @param tags      标签列表
     */
    void saveArticleTags(Long articleId, List<Tag> tags);

    /**
     * 根据标签名称查找文章内容列表ID
     *
     * @param tagEnName 标签名称
     * @return
     */
    List<Long> getArticleIdByTagName(String tagEnName);
}
