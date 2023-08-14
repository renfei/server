package net.renfei.server.cms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.cms.constant.ArticleStatusEnum;
import net.renfei.server.cms.entity.ArticleCategory;
import net.renfei.server.cms.repositories.CmsArticleCategoryMapper;
import net.renfei.server.cms.repositories.CmsArticlesMapper;
import net.renfei.server.cms.repositories.entity.CmsArticleCategory;
import net.renfei.server.cms.repositories.entity.CmsArticleCategoryExample;
import net.renfei.server.cms.repositories.entity.CmsArticlesExample;
import net.renfei.server.cms.service.ArticleCategoryService;
import net.renfei.server.core.service.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章分类服务
 *
 * @author renfei
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleCategoryServiceImpl extends BaseService implements ArticleCategoryService {
    private final CmsArticleCategoryMapper categoryMapper;
    private final CmsArticlesMapper cmsArticlesMapper;

    @Override
    public List<ArticleCategory> queryArticleCategoryTree(long pid) {
        CmsArticleCategoryExample example = new CmsArticleCategoryExample();
        example.createCriteria()
                .andPidEqualTo(pid);
        List<CmsArticleCategory> cmsArticleCategories = categoryMapper.selectByExample(example);
        List<ArticleCategory> articleCategories = new ArrayList<>(cmsArticleCategories.size());
        if (!cmsArticleCategories.isEmpty()) {
            for (CmsArticleCategory cmsArticleCategory : cmsArticleCategories) {
                ArticleCategory articleCategory = this.convert(cmsArticleCategory);
                articleCategory.setChild(this.queryArticleCategoryTree(cmsArticleCategory.getId()));
                articleCategories.add(articleCategory);
            }
        }
        return articleCategories;
    }

    @Override
    public ArticleCategory queryArticleCategoryByName(String enName) {
        if (StringUtils.hasLength(enName)) {
            CmsArticleCategoryExample example = new CmsArticleCategoryExample();
            example.createCriteria()
                    .andCategoryEnNameEqualTo(enName.toLowerCase());
            List<CmsArticleCategory> cmsArticleCategories = categoryMapper.selectByExample(example);
            if (!cmsArticleCategories.isEmpty()) {
                return this.convert(cmsArticleCategories.get(0));
            }
        }
        return null;
    }

    @Override
    public ArticleCategory queryArticleCategoryById(Long id) {
        if (id == null) {
            return null;
        }
        return this.convert(categoryMapper.selectByPrimaryKey(id));
    }

    public List<Long> queryAllChildCategoryId(Long id) {
        List<Long> ids = new ArrayList<>();
        CmsArticleCategory cmsArticleCategory = categoryMapper.selectByPrimaryKey(id);
        if (cmsArticleCategory != null) {
            ids.add(id);
            CmsArticleCategoryExample example = new CmsArticleCategoryExample();
            example.createCriteria()
                    .andPidEqualTo(id);
            List<CmsArticleCategory> cmsArticleCategories = categoryMapper.selectByExample(example);
            if (!cmsArticleCategories.isEmpty()) {
                for (CmsArticleCategory articleCategory : cmsArticleCategories) {
                    ids.addAll(this.queryAllChildCategoryId(articleCategory.getId()));
                }
            }
        }
        return ids;
    }

    @Override
    public void createArticleCategory(ArticleCategory articleCategory) {
        Assert.notNull(articleCategory, "请求体不能为空");
        Assert.notNull(articleCategory.getCategoryEnName(), "分类英文名不能为空");
        Assert.notNull(articleCategory.getCategoryZhName(), "分类中文名不能为空");
        articleCategory.setCategoryEnName(articleCategory.getCategoryEnName().toLowerCase());
        CmsArticleCategoryExample example = new CmsArticleCategoryExample();
        example.createCriteria()
                .andCategoryEnNameEqualTo(articleCategory.getCategoryEnName());
        Assert.isTrue(categoryMapper.selectByExample(example).isEmpty(), "分类英文名已经被占用，请更换一个");
        if (articleCategory.getPid() != null
                && articleCategory.getPid() != 0) {
            Assert.notNull(categoryMapper.selectByPrimaryKey(articleCategory.getPid()), "根据PID未能找到父级分类数据");
        } else {
            articleCategory.setPid(0L);
        }
        articleCategory.setId(null);
        categoryMapper.insertSelective(this.convert(articleCategory));
    }

    @Override
    public void updateArticleCategory(Long id, ArticleCategory articleCategory) {
        Assert.notNull(articleCategory, "请求体不能为空");
        Assert.notNull(articleCategory.getCategoryEnName(), "分类英文名不能为空");
        Assert.notNull(articleCategory.getCategoryZhName(), "分类中文名不能为空");
        articleCategory.setCategoryEnName(articleCategory.getCategoryEnName().toLowerCase());
        CmsArticleCategory oldArticleCategory = categoryMapper.selectByPrimaryKey(id);
        Assert.notNull(oldArticleCategory, "根据ID未能找到文章分类数据");
        if (!oldArticleCategory.getCategoryEnName().equals(articleCategory.getCategoryEnName())) {
            // 英文名产生了变更，需要检查重复情况
            CmsArticleCategoryExample example = new CmsArticleCategoryExample();
            example.createCriteria()
                    .andCategoryEnNameEqualTo(articleCategory.getCategoryEnName());
            Assert.isTrue(categoryMapper.selectByExample(example).isEmpty(),
                    "分类英文名已经被占用，请更换一个");
        }
        if (!oldArticleCategory.getPid().equals(articleCategory.getPid())) {
            // 父级ID产生了变更，需要检查
            Assert.notNull(categoryMapper.selectByPrimaryKey(articleCategory.getPid()),
                    "根据PID未能找到父级分类数据");
        }
        oldArticleCategory.setPid(articleCategory.getPid());
        oldArticleCategory.setCategoryEnName(articleCategory.getCategoryEnName());
        oldArticleCategory.setCategoryZhName(articleCategory.getCategoryZhName());
        oldArticleCategory.setDescription(articleCategory.getDescription());
        categoryMapper.updateByPrimaryKey(oldArticleCategory);
    }

    @Override
    public void deleteArticleCategory(Long id) {
        CmsArticleCategory oldArticleCategory = categoryMapper.selectByPrimaryKey(id);
        Assert.notNull(oldArticleCategory, "根据ID未能找到文章分类数据");
        CmsArticleCategoryExample example = new CmsArticleCategoryExample();
        example.createCriteria()
                .andPidEqualTo(id);
        Assert.isTrue(categoryMapper.selectByExample(example).isEmpty(),
                "该分类下还存在子级分类，无法删除");
        CmsArticlesExample articlesExample = new CmsArticlesExample();
        articlesExample.createCriteria()
                .andCategoryIdEqualTo(id)
                .andStatusNotEqualTo(ArticleStatusEnum.DELETED.toString());
        Assert.isTrue(cmsArticlesMapper.selectByExample(articlesExample).isEmpty(),
                "该分类下还存在文章内容，无法删除");
        categoryMapper.deleteByPrimaryKey(id);
    }

    private ArticleCategory convert(CmsArticleCategory cmsArticleCategory) {
        if (cmsArticleCategory == null) {
            return null;
        }
        ArticleCategory articleCategory = new ArticleCategory();
        BeanUtils.copyProperties(cmsArticleCategory, articleCategory);
        return articleCategory;
    }

    private CmsArticleCategory convert(ArticleCategory articleCategory) {
        if (articleCategory == null) {
            return null;
        }
        CmsArticleCategory cmsArticleCategory = new CmsArticleCategory();
        BeanUtils.copyProperties(articleCategory, cmsArticleCategory);
        return cmsArticleCategory;
    }
}
