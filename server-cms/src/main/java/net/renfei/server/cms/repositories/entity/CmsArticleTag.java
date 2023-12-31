package net.renfei.server.cms.repositories.entity;

import java.io.Serializable;

/**
 * Database Table Remarks:
 *   文章内容标签关系表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table cms_article_tag
 */
public class CmsArticleTag implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_article_tag.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * Database Column Remarks:
     *   标签ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_article_tag.tag_id
     *
     * @mbg.generated
     */
    private Long tagId;

    /**
     * Database Column Remarks:
     *   文章ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_article_tag.article_id
     *
     * @mbg.generated
     */
    private Long articleId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cms_article_tag
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cms_article_tag.id
     *
     * @return the value of cms_article_tag.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cms_article_tag.id
     *
     * @param id the value for cms_article_tag.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cms_article_tag.tag_id
     *
     * @return the value of cms_article_tag.tag_id
     *
     * @mbg.generated
     */
    public Long getTagId() {
        return tagId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cms_article_tag.tag_id
     *
     * @param tagId the value for cms_article_tag.tag_id
     *
     * @mbg.generated
     */
    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cms_article_tag.article_id
     *
     * @return the value of cms_article_tag.article_id
     *
     * @mbg.generated
     */
    public Long getArticleId() {
        return articleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cms_article_tag.article_id
     *
     * @param articleId the value for cms_article_tag.article_id
     *
     * @mbg.generated
     */
    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_tag
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", tagId=").append(tagId);
        sb.append(", articleId=").append(articleId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_tag
     *
     * @mbg.generated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CmsArticleTag other = (CmsArticleTag) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTagId() == null ? other.getTagId() == null : this.getTagId().equals(other.getTagId()))
            && (this.getArticleId() == null ? other.getArticleId() == null : this.getArticleId().equals(other.getArticleId()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_tag
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTagId() == null) ? 0 : getTagId().hashCode());
        result = prime * result + ((getArticleId() == null) ? 0 : getArticleId().hashCode());
        return result;
    }
}