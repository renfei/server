package net.renfei.server.cms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sankuai.inf.leaf.IDGen;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.cms.constant.ArticleStatusEnum;
import net.renfei.server.cms.entity.Article;
import net.renfei.server.cms.repositories.CmsArticleVersionsMapper;
import net.renfei.server.cms.repositories.CmsArticlesMapper;
import net.renfei.server.cms.repositories.entity.CmsArticleVersions;
import net.renfei.server.cms.repositories.entity.CmsArticles;
import net.renfei.server.cms.repositories.entity.CmsArticlesExample;
import net.renfei.server.cms.service.ArticleCategoryService;
import net.renfei.server.cms.service.ArticleService;
import net.renfei.server.core.config.ServerProperties;
import net.renfei.server.core.constant.SecretLevelEnum;
import net.renfei.server.core.entity.ListData;
import net.renfei.server.core.entity.UserDetail;
import net.renfei.server.core.service.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文章服务
 *
 * @author renfei
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends BaseService implements ArticleService {
    private final ArticleCategoryService categoryService;
    /**
     * 使用正则表达式匹配中文字符（包括英文单词）
     */
    private final static Pattern PATTERN_ZH_EN = Pattern.compile("[\\p{IsHan}\\p{IsLatin}]+");
    /**
     * Words per minute 平均阅读速度
     */
    private final static int WPM = 275;
    private final IDGen idGen;
    private final ServerProperties serverProperties;
    private final CmsArticlesMapper cmsArticlesMapper;
    private final CmsArticleVersionsMapper cmsArticleVersionsMapper;

    @Override
    public ListData<Article> queryArticleList(Long id, String title, Long categoryId, String authorName,
                                              Date publishDateMin, Date publishDateMax, ArticleStatusEnum status,
                                              SecretLevelEnum secretLevel, int pages, int rows) {
        CmsArticlesExample example = new CmsArticlesExample();
        example.setOrderByClause("publishDate DESC");
        CmsArticlesExample.Criteria criteria = example.createCriteria();
        UserDetail currentUserDetail = getCurrentUserDetail();
        if (currentUserDetail == null) {
            // 未登录，只能查询非密已发布内容
            criteria.andSecretLevelEqualTo(SecretLevelEnum.UNCLASSIFIED.getLevel())
                    .andStatusEqualTo(ArticleStatusEnum.PUBLISH.toString());
        } else {
            if (secretLevel != null) {
                if (SecretLevelEnum.outOfSecretLevel(currentUserDetail.getSecretLevel(), secretLevel)) {
                    criteria.andSecretLevelLessThanOrEqualTo(currentUserDetail.getSecretLevel().getLevel());
                } else {
                    criteria.andSecretLevelEqualTo(secretLevel.getLevel());
                }
            } else {
                criteria.andSecretLevelLessThanOrEqualTo(currentUserDetail.getSecretLevel().getLevel());
            }
        }
        if (id != null) {
            criteria.andIdEqualTo(id);
        }
        if (StringUtils.hasLength(title)) {
            criteria.andTitleLike("%" + title + "%");
        }
        if (categoryId != null) {
            criteria.andCategoryIdEqualTo(categoryId);
        }
        if (StringUtils.hasLength(authorName)) {
            criteria.andAuthorNameLike("%" + authorName + "%");
        }
        if (publishDateMin != null) {
            criteria.andPublishDateGreaterThanOrEqualTo(publishDateMin);
        }
        if (publishDateMax != null) {
            criteria.andPublishDateLessThanOrEqualTo(publishDateMax);
        }
        if (status != null) {
            criteria.andStatusEqualTo(status.toString());
        }
        try (Page<CmsArticles> page = PageHelper.startPage(pages, rows)) {
            cmsArticlesMapper.selectByExampleWithBLOBs(example);
            List<Article> articles = new ArrayList<>(page.size());
            page.forEach(cmsArticles -> articles.add(this.convert(cmsArticles)));
            return new ListData<>(page, articles);
        }
    }

    @Override
    public Article createArticle(Article article) {
        Assert.hasLength(article.getTitle(), "文章标题不能为空");
        Assert.hasLength(article.getContent(), "文章内容不能为空");
        article.setId(idGen.get("").getId());
        article.setAuthorId(Long.valueOf(getCurrentUserDetail().getId()));
        article.setAuthorName(getCurrentUserDetail().getUsername());
        article.setUpdateDate(new Date());
        // TODO 检查分类是否存在
        // TODO 根据是否开启审核流程确定状态
        article.setStatus(ArticleStatusEnum.REVIEW);
        article.setViews(0L);
        article.setVersions(1);
        article.setSecretLevel(SecretLevelEnum.UNCLASSIFIED);
        article.setThumbsUp(0L);
        article.setThumbsDown(0L);
        article.setReadTime(this.getReadTime(this.getPlainText(article.getContent())));
        article.setWordCount(this.getWordCount(this.getPlainText(article.getContent())));
        cmsArticlesMapper.insert(this.convert(article));
        return article;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(Long id, Article article) {
        CmsArticles cmsArticles = cmsArticlesMapper.selectByPrimaryKey(id);
        Assert.notNull(cmsArticles, "根据ID未能找到对应的数据");
        Assert.hasLength(article.getTitle(), "文章标题不能为空");
        Assert.hasLength(article.getContent(), "文章内容不能为空");
        // 将旧数据存储版本表
        CmsArticleVersions cmsArticleVersions = new CmsArticleVersions();
        BeanUtils.copyProperties(cmsArticles, cmsArticleVersions);
        cmsArticleVersions.setId(idGen.get("").getId());
        cmsArticleVersions.setArticleId(cmsArticles.getId());
        cmsArticleVersionsMapper.insert(cmsArticleVersions);
        if (!cmsArticles.getCategoryId().equals(article.getCategoryId())) {
            // TODO 检查分类是否存在
        }
        // 修改文章，重新设置为审核状态，需要重新上线
        cmsArticles.setStatus(ArticleStatusEnum.REVIEW.toString());
        cmsArticles.setTitle(article.getTitle());
        cmsArticles.setKeyword(article.getKeyword());
        cmsArticles.setDescription(article.getDescription());
        cmsArticles.setCategoryId(article.getCategoryId());
        cmsArticles.setCoverImage(article.getCoverImage());
        cmsArticles.setPublishDate(article.getPublishDate());
        cmsArticles.setUpdateDate(new Date());
        cmsArticles.setCommentStatus(article.getCommentStatus());
        cmsArticles.setPassword(article.getPassword());
        cmsArticles.setVersions(cmsArticleVersions.getVersions() + 1);
        cmsArticles.setIsOriginal(article.getIsOriginal());
        cmsArticles.setIndexNumber(article.getIndexNumber());
        cmsArticles.setDocumentIssuingAgency(article.getDocumentIssuingAgency());
        cmsArticles.setWrittenDate(article.getWrittenDate());
        cmsArticles.setIssuedNumber(article.getIssuedNumber());
        cmsArticles.setOfficialDocCategory(article.getOfficialDocCategory());
        cmsArticles.setSource(article.getSource());
        cmsArticles.setSourceUrl(article.getSourceUrl());
        cmsArticles.setReadTime(this.getReadTime(this.getPlainText(article.getContent())));
        cmsArticles.setWordCount(this.getWordCount(this.getPlainText(article.getContent())));
        cmsArticles.setContent(article.getContent());
        cmsArticlesMapper.updateByPrimaryKeyWithBLOBs(cmsArticles);
    }

    @Override
    public void publishArticle(Long id) {
        CmsArticles cmsArticles = cmsArticlesMapper.selectByPrimaryKey(id);
        Assert.notNull(cmsArticles, "根据ID未能找到对应的数据");
        Assert.isTrue(
                ArticleStatusEnum.REVIEW.equals(ArticleStatusEnum.valueOf(cmsArticles.getStatus()))
                        || ArticleStatusEnum.OFFLINE.equals(ArticleStatusEnum.valueOf(cmsArticles.getStatus()))
                , "文章当前状态无法发布");
        cmsArticles.setStatus(ArticleStatusEnum.PUBLISH.toString());
        cmsArticlesMapper.updateByPrimaryKeyWithBLOBs(cmsArticles);
    }

    @Override
    public void offlineArticle(Long id) {
        CmsArticles cmsArticles = cmsArticlesMapper.selectByPrimaryKey(id);
        Assert.notNull(cmsArticles, "根据ID未能找到对应的数据");
        Assert.isTrue(ArticleStatusEnum.PUBLISH.equals(ArticleStatusEnum.valueOf(cmsArticles.getStatus()))
                , "文章当前状态并未上线，无法下线操作");
        cmsArticles.setStatus(ArticleStatusEnum.OFFLINE.toString());
        cmsArticlesMapper.updateByPrimaryKeyWithBLOBs(cmsArticles);
    }

    @Override
    public void deleteArticle(Long id) {
        CmsArticles cmsArticles = cmsArticlesMapper.selectByPrimaryKey(id);
        Assert.notNull(cmsArticles, "根据ID未能找到对应的数据");
        cmsArticles.setStatus(ArticleStatusEnum.DELETED.toString());
        cmsArticlesMapper.updateByPrimaryKeyWithBLOBs(cmsArticles);
    }

    @Override
    public void settingArticleSecretLevel(Long id, SecretLevelEnum secretLevel) {
        Assert.notNull(secretLevel, "密级不能为空");
        Assert.isTrue(!SecretLevelEnum.outOfSecretLevel(serverProperties.getSystemMaxSecretLevel(), secretLevel)
                , "设置的密级不能大于系统允许的最大密级");
        Assert.isTrue(!SecretLevelEnum.outOfSecretLevel(getCurrentUserDetail().getSecretLevel(), secretLevel)
                , "设置的密级不能大于您自己账号的密级");
        CmsArticles cmsArticles = cmsArticlesMapper.selectByPrimaryKey(id);
        Assert.notNull(cmsArticles, "根据ID未能找到对应的数据");
        cmsArticles.setSecretLevel(secretLevel.getLevel());
        cmsArticlesMapper.updateByPrimaryKeyWithBLOBs(cmsArticles);
    }

    @Override
    public String getReadTime(String text) {
        long wordCount = this.getWordCount(text);
        long minute = wordCount / WPM;
        return minute + "分钟";
    }

    @Override
    public long getWordCount(String text) {
        Matcher matcher = PATTERN_ZH_EN.matcher(text);
        long count = 0;
        while (matcher.find()) {
            count += matcher.group().length();
        }
        return count;
    }

    @Override
    public String getPlainText(String html) {
        Document doc = Jsoup.parse(html);
        return doc.text();
    }

    private CmsArticles convert(Article article) {
        if (article == null) {
            return null;
        }
        CmsArticles cmsArticles = new CmsArticles();
        BeanUtils.copyProperties(article, cmsArticles);
        cmsArticles.setStatus(article.getStatus().toString());
        cmsArticles.setSecretLevel(article.getSecretLevel().getLevel());
        return cmsArticles;
    }

    private Article convert(CmsArticles cmsArticles) {
        if (cmsArticles == null) {
            return null;
        }
        Article article = new Article();
        BeanUtils.copyProperties(cmsArticles, article);
        article.setStatus(ArticleStatusEnum.valueOf(cmsArticles.getStatus()));
        article.setSecretLevel(SecretLevelEnum.valueOf(cmsArticles.getSecretLevel()));
        return article;
    }
}
