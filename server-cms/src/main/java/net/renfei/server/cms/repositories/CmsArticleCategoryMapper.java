package net.renfei.server.cms.repositories;

import java.util.List;
import net.renfei.server.cms.repositories.entity.CmsArticleCategory;
import net.renfei.server.cms.repositories.entity.CmsArticleCategoryExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CmsArticleCategoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    long countByExample(CmsArticleCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    int deleteByExample(CmsArticleCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    int insert(CmsArticleCategory row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    int insertSelective(CmsArticleCategory row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    List<CmsArticleCategory> selectByExample(CmsArticleCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    CmsArticleCategory selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("row") CmsArticleCategory row, @Param("example") CmsArticleCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    int updateByExample(@Param("row") CmsArticleCategory row, @Param("example") CmsArticleCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(CmsArticleCategory row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(CmsArticleCategory row);
}