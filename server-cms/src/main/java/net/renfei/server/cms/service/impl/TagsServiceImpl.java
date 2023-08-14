package net.renfei.server.cms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.cms.entity.Tag;
import net.renfei.server.cms.repositories.CmsArticleTagMapper;
import net.renfei.server.cms.repositories.CmsTagsMapper;
import net.renfei.server.cms.repositories.entity.CmsArticleTag;
import net.renfei.server.cms.repositories.entity.CmsArticleTagExample;
import net.renfei.server.cms.repositories.entity.CmsTags;
import net.renfei.server.cms.repositories.entity.CmsTagsExample;
import net.renfei.server.cms.service.TagsService;
import net.renfei.server.core.service.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签服务
 *
 * @author renfei
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TagsServiceImpl extends BaseService implements TagsService {
    private final CmsTagsMapper tagsMapper;
    private final CmsArticleTagMapper articleTagMapper;

    public List<Tag> queryTagList(String enName, String zhName) {
        CmsTagsExample example = new CmsTagsExample();
        CmsTagsExample.Criteria criteria = example.createCriteria();
        if (StringUtils.hasLength(enName)) {
            criteria.andTagEnNameEqualTo(enName.toLowerCase());
        }
        if (StringUtils.hasLength(zhName)) {
            criteria.andTagZhNameLike("%" + zhName + "%");
        }
        List<CmsTags> cmsTags = tagsMapper.selectByExample(example);
        List<Tag> tags = new ArrayList<>(cmsTags.size());
        for (CmsTags cmsTag : cmsTags) {
            tags.add(this.convert(cmsTag));
        }
        return tags;
    }

    @Override
    public void createTag(Tag tag) {
        Assert.notNull(tag, "请求体不能为空");
        Assert.notNull(tag.getTagEnName(), "标签英文名不能为空");
        Assert.notNull(tag.getTagZhName(), "标签中文名不能为空");
        tag.setTagEnName(tag.getTagEnName().toLowerCase());
        CmsTagsExample example = new CmsTagsExample();
        example.createCriteria()
                .andTagEnNameEqualTo(tag.getTagEnName());
        Assert.isTrue(tagsMapper.selectByExample(example).isEmpty(), "标签英文名已经被占用，请更换一个");
        tag.setId(null);
        tagsMapper.insertSelective(this.convert(tag));
    }

    @Override
    public void updateTag(Long id, Tag tag) {
        Assert.notNull(tag, "请求体不能为空");
        Assert.notNull(tag.getTagEnName(), "标签英文名不能为空");
        Assert.notNull(tag.getTagZhName(), "标签中文名不能为空");
        CmsTags oldCmsTags = tagsMapper.selectByPrimaryKey(id);
        if (!oldCmsTags.getTagEnName().equals(tag.getTagEnName())) {
            // 英文名发生了变更
            CmsTagsExample example = new CmsTagsExample();
            example.createCriteria()
                    .andTagEnNameEqualTo(tag.getTagEnName());
            Assert.isTrue(tagsMapper.selectByExample(example).isEmpty(), "标签英文名已经被占用，请更换一个");
        }
        CmsTags convert = this.convert(tag);
        convert.setId(id);
        tagsMapper.updateByPrimaryKey(convert);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTag(Long id) {
        CmsArticleTagExample example = new CmsArticleTagExample();
        example.createCriteria()
                .andTagIdEqualTo(id);
        articleTagMapper.deleteByExample(example);
        tagsMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveArticleTags(Long articleId, List<Tag> tags) {
        // 先删除再插入
        CmsArticleTagExample example = new CmsArticleTagExample();
        example.createCriteria()
                .andArticleIdEqualTo(articleId);
        articleTagMapper.deleteByExample(example);
        for (Tag tag : tags) {
            CmsArticleTag cmsArticleTag = new CmsArticleTag();
            cmsArticleTag.setArticleId(articleId);
            cmsArticleTag.setTagId(tag.getId());
            articleTagMapper.insertSelective(cmsArticleTag);
        }
    }

    public List<Long> getArticleIdByTagName(String tagEnName) {
        List<Tag> tags = this.queryTagList(tagEnName, null);
        if (tags.isEmpty()) {
            return null;
        }
        Tag tag = tags.get(0);
        CmsArticleTagExample example = new CmsArticleTagExample();
        example.createCriteria()
                .andTagIdEqualTo(tag.getId());
        List<CmsArticleTag> cmsArticleTags = articleTagMapper.selectByExample(example);
        List<Long> ids = new ArrayList<>(cmsArticleTags.size());
        for (CmsArticleTag cmsArticleTag : cmsArticleTags) {
            ids.add(cmsArticleTag.getArticleId());
        }
        return ids;
    }

    private CmsTags convert(Tag tag) {
        if (tag == null) {
            return null;
        }
        CmsTags cmsTags = new CmsTags();
        BeanUtils.copyProperties(tag, cmsTags);
        return cmsTags;
    }

    private Tag convert(CmsTags cmsTags) {
        if (cmsTags == null) {
            return null;
        }
        Tag tag = new Tag();
        BeanUtils.copyProperties(cmsTags, tag);
        return tag;
    }
}
